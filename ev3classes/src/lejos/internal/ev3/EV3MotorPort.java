package lejos.internal.ev3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.hardware.motor.JavaMotorRegulator;
import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.internal.io.NativeDevice;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public class EV3MotorPort extends EV3IOPort implements TachoMotorPort {
    static final byte OUTPUT_SET_TYPE = (byte)0xa1;
    static final byte OUTPUT_POWER = (byte)0xa4;
    static final byte OUTPUT_START = (byte)0xa6;
    static final byte OUTPUT_STOP = (byte)0xa3;
    static final byte OUTPUT_CLR_COUNT = (byte)0xb2;
        
    protected static NativeDevice tacho;
    protected static volatile ByteBuffer bbuf;
    protected static volatile IntBuffer ibuf;
    protected static NativeDevice pwm;
    static
    {
        initDeviceIO();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {resetAll();}
        });
    }
    protected int curMode = FLOAT+1; // current mode is unknown
    protected byte[] cmd = new byte[55];
    protected MotorRegulator regulator;

    /**
     * Implementation of a PID based motor regulator that uses a kernel module
     * for the core regulation operations. This mechanism is accessed via the
     * EV3MotorPort class.
     **/
    public class EV3MotorRegulatorKernelModule extends Thread implements MotorRegulator
    {
        static final int NO_LIMIT = 0x7fffffff;
        static final int ST_IDLE = 0;
        static final int ST_STALL = 1;
        static final int ST_HOLD = 2;
        static final int ST_START = 3;
        static final int ST_ACCEL = 4;
        static final int ST_MOVE = 5;
        static final int ST_DECEL = 6;

        protected int zeroTachoCnt;
        protected int limitAngle;
        protected float curPosition;
        protected float curVelocity;
        protected volatile float curCnt;
        protected volatile int curTime;
        protected int curLimit;
        protected float curSpeed;
        protected float curAcc;
        protected boolean curHold;
        protected int stallLimit=50;
        protected int stallTime=1000;
        // state for listener stuff
        boolean started = false;
        RegulatedMotorListener listener;
        RegulatedMotor motor;

        public EV3MotorRegulatorKernelModule(TachoMotorPort p)
        {
            if (p != EV3MotorPort.this)
                throw new IllegalArgumentException("Invlaid port specified");
            // don't wait for the listener thread to finish
            this.setDaemon(true);
        }
        
        // Fixed point routines and constants
        static final int FIX_SCALE = 256;
        
        protected int floatToFix(float f)
        {
            return Math.round(f*FIX_SCALE);
        }
        
        protected int intToFix(int i)
        {
            return i*FIX_SCALE;
        }
        
        protected float FixToFloat(int fix)
        {
            return (float)fix/FIX_SCALE;
        }
        
        protected int FixMult(int a, int b)
        {
            return (a*b)/FIX_SCALE;
        }
        
        protected int FixDiv(int a, int b)
        {
            return (a*FIX_SCALE)/b;
        }
        
        protected int FixRound(int a)
        {
            return (a >= 0 ? (a+FIX_SCALE/2)/FIX_SCALE : (a-FIX_SCALE/2)/FIX_SCALE);
        }

       

        /**
         * pack a value ready to be written to the kernel module
         * @param buf
         * @param offset
         * @param val
         */
        protected void setVal(byte[] buf, int offset, int val)
        {
            buf[offset] = (byte)val;
            buf[offset+1] = (byte)(val >> 8);
            buf[offset+2] = (byte)(val >> 16);
            buf[offset+3] = (byte)(val >> 24);
        }

        /**
         * Set the PID control parameters in the kernel module
         * @param typ
         * @param moveP
         * @param moveI
         * @param moveD
         * @param holdP
         * @param holdI
         * @param holdD
         * @param offset
         * @param deadBand
         */
        public void setControlParams(int typ, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset, float deadBand)
        {
            cmd[0] = OUTPUT_SET_TYPE;
            cmd[1] = (byte)port;
            cmd[2] = (byte)typ;
            setVal(cmd, 3, floatToFix(moveP));
            setVal(cmd, 7, floatToFix(moveI));
            setVal(cmd, 11, floatToFix(moveD));
            setVal(cmd, 15, floatToFix(holdP));
            setVal(cmd, 19, floatToFix(holdI));
            setVal(cmd, 23, floatToFix(holdD));
            setVal(cmd, 27, offset);
            setVal(cmd, 31, floatToFix(deadBand));
            pwm.write(cmd, 35);
            
        }

        
        protected synchronized void checkComplete()
        {
            if (started && !isMoving())
            {
                started = false;
                if (listener != null)
                    listener.rotationStopped(motor, getTachoCount(), isStalled(), System.currentTimeMillis());
            }
        }

        protected synchronized void startNewMove()
        {
            if (started)
                checkComplete();
            if (started)
                throw new IllegalStateException("Motor must be stopped");
            started = true;
            if (listener != null)
            {
                listener.rotationStarted(motor, getTachoCount(), false, System.currentTimeMillis());
                notifyAll();
            }
                
        }
        
        public synchronized void run()
        {
            while (true)
            {
                while (!started)
                    try {
                        wait();
                    } catch (InterruptedException e){}
                checkComplete();
                try {
                    wait(5);
                } catch (InterruptedException e){}
            }
        }
        
        /**
         * Start a move using the PID loop in the kernel module
         * @param t1 Time for acceleration phase
         * @param t2 Time for cruise phase
         * @param t3 Time for deceleration phase
         * @param c2 Position (cnt) after acceleration phase
         * @param c3 Position (cnt) after cruise stage
         * @param v1 Velocity at start of acceleration stage
         * @param v2 Velocity after acceleration stage
         * @param a1 Acceleration
         * @param a3 Deceleration
         * @param sl stall limit
         * @param st stall time
         * @param ts Time stamp
         * @param hold What to do after the move
         */
        protected void subMove(int t1, int t2, int t3, float c1, float c2, float c3, float v1, float v2, float a1, float a3, int sl, int st, int ts, boolean hold)
        {
            //System.out.println("t1 " + t1 + " t2 " + t2 + " t3 " + t3 + " c1 " + c1 + " c2 " + c2 + " c3 " + c3 + " v1 " + v1 + " v2 " + v2 + " a1 " + a1 + " a3 " + a3);
            // convert units from /s (i.e 100ms) to be per 1024ms to allow div to be performed by shift
//System.out.println("c1 " + c1 + " c2 " + c2 + " ts " + ts + " ct " + System.currentTimeMillis());
            v1 = (v1/1000f)*1024f;
            v2 = (v2/1000f)*1024f;
            a1 = (((a1/1000f)*1024f)/1000f)*1024f;
            a3 = (((a3/1000f)*1024f)/1000f)*1024f;
            // now start the actual move
            cmd[0] = OUTPUT_START;
            cmd[1] = (byte)port;
            setVal(cmd, 2, t1);
            setVal(cmd, 6, t2);
            setVal(cmd, 10, t3);
            setVal(cmd, 14, floatToFix(c1));
            setVal(cmd, 18, floatToFix(c2));
            setVal(cmd, 22, floatToFix(c3));
            setVal(cmd, 26, floatToFix(v1));
            setVal(cmd, 30, floatToFix(v2));
            setVal(cmd, 34, floatToFix(a1));
            setVal(cmd, 38, floatToFix(a3));
            setVal(cmd, 42, sl);
            setVal(cmd, 46, st);
            setVal(cmd, 50, ts);
            cmd[54] = (byte) (hold ? 1 : 0);
            if ((v1 != 0 || v2 != 0) && !isMoving())
                startNewMove();
            pwm.write(cmd, 55);      
        }
        
        /**
         * Helper method generate a move by splitting it into three phases, initial
         * acceleration, constant velocity, and final deceleration. We allow for the case
         * were it is not possible to reach the required constant velocity and hence the
         * move becomes triangular rather than trapezoid.   
         * @param curVel Initial velocity
         * @param curPos Initial position
         * @param speed
         * @param acc
         * @param limit
         * @param hold
         */
        protected void genMove(float curVel, float curPos, float curCnt, int curTime, float speed, float acc, int limit, boolean hold)
        {
            // Save current move params
            curSpeed = speed;
            curHold = hold;
            curAcc = acc;
            curLimit = limit;
            float u2 = curVel*curVel;
            int len = (int)(limit - curPos);
            float v = speed;
            float a1 = acc;
            float a3 = acc;
            //System.out.println("limit " + limit + " len " + len + " speed " + speed + " hold " + hold);
            if (speed == 0.0)
            {
                //System.out.println("Stop");
                if (curVel < 0)
                    a3 = -a3;
                // Stop case
                float s3 = (u2)/(2*a3);
                int t3 = (int)(1000*(2*(s3))/(curVel));
                subMove(0, 0, t3, 0, 0, curCnt, 0, curVel, 0, -a3, stallLimit, stallTime, curTime, hold);
                return;
            }
            if (len < 0)
            {
                a3 = -a3;
                a1 = -a1;
                v = -speed;
            }
            float v2 = v*v;
            if (v2 < u2)
                a1 = -a1;
            if (Math.abs(limit) == NO_LIMIT)
            {
                //System.out.println("Unlimited move");
                // Run forever, no need for deceleration
                float s1 = (v2 - u2)/(2*a1);
                int t1 = (int)(1000*(2*s1)/(curVel + v));
                subMove(t1, NO_LIMIT, 0, curCnt, curCnt + s1, 0, curVel, v, a1, 0, stallLimit, stallTime, curTime, hold);
                return;
            }
            float vmax2 = a3*len + u2/2;
            if (vmax2 <= v2)
            {
                //System.out.println("Triangle");
                v = (float) Math.sqrt(vmax2);
                if (len < 0)
                {
                    v = -v;
                }
                // triangular move
                float s1 = (vmax2 - u2)/(2*a1);
                int t1 = (int)(1000*(2*s1)/(curVel + v));
                int t2 = t1;
                int t3 = t2 + (int)(1000*(2*(len-s1))/(v));
                subMove(t1, t2, t3, curCnt, 0, s1+curCnt, curVel, v, a1, -a3, stallLimit, stallTime, curTime, hold);         
            }
            else
            {
                //System.out.println("Trap");
                // trapezoid move
                float s1 = (v2 - u2)/(2*a1);
                float s3 = (v2)/(2*a3);
                float s2 = len - s1 - s3;
                //System.out.println("s1 " + s1 + " s2 " + s2 + " s3 " + s3);
                int t1 = (int)(1000*(2*s1)/(curVel + v));
                int t2 = t1 + (int)(1000*s2/v);
                int t3 = t2 + (int)(1000*(2*(s3))/(v));
                //System.out.println("v " + v + " a1 " + a1 + " a3 " + (-a3));
                subMove(t1, t2, t3, curCnt, curCnt+s1, curCnt+s1+s2, curVel, v, a1, -a3, stallLimit, stallTime, curTime, hold);

            }
        }

        /**
         * Waits for the current move operation to complete
         */
        public void waitComplete()
        {
            /*
            int pos = this.getTachoCount();
            while(isMoving())
            {
                Delay.msDelay(1);
                int newPos = this.getTachoCount();
                if (newPos != pos)
                {
                    updateVelocityAndPosition();
                    System.out.println("P: " + newPos + " T " + curPosition + " V " + curVelocity);
                    pos = newPos;
                }
            }*/
            while(isMoving())
                Delay.msDelay(1);
            checkComplete();
                
        }

        
        /**
         * Initiate a new move and optionally wait for it to complete.
         * If some other move is currently executing then ensure that this move
         * is terminated correctly and then start the new move operation.
         * @param speed
         * @param acceleration
         * @param limit
         * @param hold
         * @param waitComplete
         */
        synchronized public void newMove(float speed, int acceleration, int limit, boolean hold, boolean waitComplete)
        {
            limitAngle = limit;
            if (Math.abs(limit) != NO_LIMIT)
                limit += zeroTachoCnt;
            // Ignore repeated commands
            if (!waitComplete && (speed == curSpeed) && (curAcc == acceleration) && (curLimit == limit) && (curHold == hold))
                return;
//System.out.println("New command " + speed + " ismoving " + getRegState());
            updateVelocityAndPosition();
            // Stop moves always happen now
            if (speed == 0)
                genMove(curVelocity, curPosition, curCnt, curTime, 0, acceleration, NO_LIMIT, hold);
            else if (!isMoving())
            {
                // not moving so we start a new move
                genMove(curVelocity, curPosition, curCnt, 0, speed, acceleration, limit, hold);
            }
            else
            {
                // we already have a move in progress can we modify it to match
                // the new request? We must ensure that the new move is in the
                // same direction and that any stop will not exceed the current
                // acceleration request.
                float moveLen = limit - curPosition;
                float acc = (curVelocity*curVelocity)/(2*(moveLen));
                if (moveLen*curVelocity >= 0 && Math.abs(acc) <= acceleration)
                    genMove(curVelocity, curPosition, curCnt, curTime, speed, acceleration, limit, hold);
                else
                {
                    System.out.println("gen two moves");
                    genMove(curVelocity, curPosition, curCnt, curTime, 0, acceleration, NO_LIMIT, true);
                    waitComplete();
                    updateVelocityAndPosition();
                    genMove(curVelocity, curPosition, curCnt, 0, speed, acceleration, limit, hold);
                }
            }
            if (waitComplete)
                waitComplete();
        }
               
        /**
         * Grabs the current state of the regulator
         */
        protected synchronized void updateVelocityAndPosition()
        {
            int baseCnt;
            int cnt;
            int vel;
            int time;
            int time2;
            int serial;
            int loopCnt = 0;
            // Check to make sure time is not changed during read
            do {
                time = ibuf.get(port*7 + 5);
                baseCnt = ibuf.get(port*7);
                cnt = ibuf.get(port*7+1);
                vel = ibuf.get(port*7+2);
                loopCnt++;
                time2 = ibuf.get(port*7 + 6);
                serial = ibuf.get(port*7 + 6);
            } while (time != time2);
            //if (loopCnt > 1) System.out.println("loop cnt " + loopCnt + " time " + time);
            curCnt = FixToFloat(cnt);
            curPosition = curCnt + baseCnt;
            curVelocity = (FixToFloat(vel)/1024)*1000;
            //if (curTime == time)
                //System.out.println("t " + time + " cc " + curCnt + " s " + serial);
            curTime = time;
        }
        
        /**
         * returns the current position from the regulator
         * @return current position in degrees
         */
        public  float getPosition()
        {
            updateVelocityAndPosition();
            return curPosition + zeroTachoCnt;
        }

        /**
         * returns the current velocity from the regulator
         * @return velocity in degrees per second
         */
        public float getCurrentVelocity()
        {
            updateVelocityAndPosition();
            return curVelocity;
        }

        
        protected synchronized int getRegState()
        {
            return ibuf.get(port*7 + 4);
        }
        
        public boolean isMoving()
        {
            return getRegState() >= ST_START;
        }
        
        public boolean isStalled()
        {
            return getRegState() == ST_STALL;
        }
                        
        public synchronized int getTachoCount()
        {
            return EV3MotorPort.this.getTachoCount() - zeroTachoCnt;
        }
        
        public void resetTachoCount()
        {
            zeroTachoCnt = EV3MotorPort.this.getTachoCount();
        }

        
        public void setStallThreshold(int error, int time)
        {
            this.stallLimit = error;
            this.stallTime = time;
        }


        /**
         * The target speed has been changed. Reflect this change in the
         * regulator.
         * @param newSpeed new target speed.
         */
        public synchronized void adjustSpeed(float newSpeed)
        {
            int state = getRegState();
            if (curSpeed != 0 && newSpeed != curSpeed && state >= ST_START && state <= ST_MOVE)
            {
                updateVelocityAndPosition();
                genMove(curVelocity, curPosition, curCnt, curTime, newSpeed, curAcc, curLimit, curHold);
            }
        }

        /**
         * The target acceleration has been changed. Updated the regulator.
         * @param newAcc
         */
        public synchronized void adjustAcceleration(int newAcc)
        {
            int state = getRegState();
            if (newAcc != curAcc && state >= ST_START && state <= ST_MOVE)
            {
                updateVelocityAndPosition();
                genMove(curVelocity, curPosition, curCnt, curTime, curSpeed, newAcc, curLimit, curHold);
            }
        }


        @Override
        public void setControlParamaters(int typ, float moveP, float moveI,
                float moveD, float holdP, float holdI, float holdD, int offset)
        {
            setControlParams(typ, moveP, moveI, moveD, holdP, holdI, holdD, offset, 0.0f);
        }



        @Override
        public void addListener(RegulatedMotor motor, RegulatedMotorListener listener)
        {
            this.motor = motor;
            this.listener = listener;
            if (getState() == Thread.State.NEW)
                start();
        }


        @Override
        public RegulatedMotorListener removeListener()
        {
            RegulatedMotorListener old = listener;
            listener = null;
            return old;
        }


        @Override
        public int getLimitAngle()
        {
            // TODO Auto-generated method stub
            return limitAngle;
        }



    }    

    protected void setPower(int power)
    {
        cmd[0] = OUTPUT_POWER;
        cmd[1] = (byte) port;
        cmd[2] = (byte) power;
        pwm.write(cmd, 3);
    }
    
    protected void start()
    {
        cmd[0] = OUTPUT_START;
        cmd[1] = (byte) port;
        pwm.write(cmd, 2);
    }
    
    protected void stop(boolean flt)
    {
        cmd[0] = OUTPUT_STOP;
        cmd[1] = (byte) port;
        cmd[2] = (byte) (flt ? 0 : 1);

    }
    
    
    /**
     * Low-level method to control a motor. 
     * 
     * @param power power from 0-100
     * @param mode defined in <code>BasicMotorPort</code>. 1=forward, 2=backward, 3=stop, 4=float.
     * @see BasicMotorPort#FORWARD
     * @see BasicMotorPort#BACKWARD
     * @see BasicMotorPort#FLOAT
     * @see BasicMotorPort#STOP
     */
    public void controlMotor(int power, int mode)
    {
        // Convert lejos power and mode to EV3 power and mode
        if (mode >= STOP)
        {
            power = 0;
            stop(mode == FLOAT);
        }
        else
        {
            if (mode == BACKWARD)
                power = -power;
            setPower(power);
        }
        curMode = mode;
    }


    /**
     * returns tachometer count
     */
    public  int getTachoCount()
    {
        return ibuf.get(port*7 + 3);
    }
    
    
    /**
     *resets the tachometer count to 0;
     */ 
    public void resetTachoCount()
    {
        synchronized(cmd)
        {
            cmd[0] = OUTPUT_CLR_COUNT;
            cmd[1] = (byte)port;
            pwm.write(cmd,  2);
        }
    }
    
    public void setPWMMode(int mode)
    {
    }

    /**
     * reset all motor ports by setting the type to be none.
     */
    private static void resetAll()
    {
        byte[] cmd = new byte[35];
        for(int i = 0; i < EV3IOPort.MOTORS; i++)
        {
            cmd[0] = OUTPUT_SET_TYPE;
            cmd[1] = (byte)i;
            cmd[2] = (byte)EV3IOPort.TYPE_NONE;
            pwm.write(cmd, 35);
        }            
    }
    
    
    private static void initDeviceIO()
    {
        tacho = new NativeDevice("/dev/lms_motor");
        bbuf = tacho.mmap(4*7*4).getByteBuffer(0, 4*7*4);
        //System.out.println("direct " + bbuf.isDirect());
        ibuf = bbuf.asIntBuffer();
        pwm = new NativeDevice("/dev/lms_pwm");
        resetAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized MotorRegulator getRegulator()
    {
        if (regulator == null)
            regulator = new EV3MotorRegulatorKernelModule(this);
            //regulator = new JavaMotorRegulator(this);
        return regulator;
    }
}
