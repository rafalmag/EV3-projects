package lejos.internal.ev3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

/**
 * 
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
    
    static final int ST_IDLE = 0;
    static final int ST_STALL = 1;
    static final int ST_HOLD = 2;
    static final int ST_START = 3;
    static final int ST_ACCEL = 4;
    static final int ST_MOVE = 5;
    static final int ST_DECEL = 6;
    
    static final int NO_LIMIT = 0x7fffffff;
    
    protected static NativeDevice tacho;
    protected static ByteBuffer bbuf;
    protected static IntBuffer ibuf;
    protected static NativeDevice pwm;
    static
    {
        initDeviceIO();
    }
    protected int curMode = FLOAT+1; // current mode is unknown
    protected byte[] cmd = new byte[43];
    protected MotorRegulator regulator;
    protected float curPosition;
    protected float curVelocity;
    protected float curCnt;
    protected int curTime;
    
    // Fixed point routines and constants
    static final int FIX_SCALE = 256;
    
    static int floatToFix(float f)
    {
        return Math.round(f*FIX_SCALE);
    }
    
    static int intToFix(int i)
    {
        return i*FIX_SCALE;
    }
    
    static float FixToFloat(int fix)
    {
        return (float)fix/FIX_SCALE;
    }
    
    static int FixMult(int a, int b)
    {
        return (a*b)/FIX_SCALE;
    }
    
    static int FixDiv(int a, int b)
    {
        return (a*FIX_SCALE)/b;
    }
    
    static int FixRound(int a)
    {
        return (a >= 0 ? (a+FIX_SCALE/2)/FIX_SCALE : (a-FIX_SCALE/2)/FIX_SCALE);
    }

   

    protected void setVal(byte[] buf, int offset, int val)
    {
        buf[offset] = (byte)val;
        buf[offset+1] = (byte)(val >> 8);
        buf[offset+2] = (byte)(val >> 16);
        buf[offset+3] = (byte)(val >> 24);
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

    public void setControlParams(int typ, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset, int deadBand)
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
        setVal(cmd, 31, intToFix(deadBand));
        pwm.write(cmd, 35);
        
    }
    


    protected void subMove(int t1, int t2, int t3, float c2, float c3, float v1, float v2, float a1, float a3, int time, boolean hold)
    {
        // convert units from /s (i.e 100ms) to be per 1024ms to allow div to be performed by shift
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
        setVal(cmd, 14, floatToFix(c2));
        setVal(cmd, 18, floatToFix(c3));
        setVal(cmd, 22, floatToFix(v1));
        setVal(cmd, 26, floatToFix(v2));
        setVal(cmd, 30, floatToFix(a1));
        setVal(cmd, 34, floatToFix(a3));
        setVal(cmd, 38, time);
        cmd[42] = (byte) (hold ? 1 : 0);
        pwm.write(cmd, 43);      
    }
    
    /**
     * Helper method generate a move by splitting it into three phases, initial
     * acceleration, constant velocity, and final deceleration. We allow for the case
     * were it is not possible to reach the required constant velocity and hence the
     * move becomes triangular rather than trapezoid.   
     * @param curVel
     * @param curPos
     * @param speed
     * @param acc
     * @param limit
     * @param hold
     */
    protected void genMove(float curVel, float curPos, float curCnt, int curTime, float speed, float acc, int limit, boolean hold)
    {
        float u2 = curVel*curVel;
        int len = (int)(limit - curPos);
        float v = speed;
        float a1 = acc;
        float a3 = acc;
        System.out.println("limit " + limit + " len " + len + " speed " + speed + " hold " + hold);
        if (speed == 0.0)
        {
            System.out.println("Stop");
            if (curVel < 0)
                a3 = -a3;
            // Stop case
            float s3 = (u2)/(2*a3);
            int t3 = (int)(1000*(2*(s3))/(curVel));
            subMove(0, 0, t3, 0, curCnt, 0, curVel, 0, -a3, curTime, hold);
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
            System.out.println("Unlimited move");
            // Run forever, no need for deceleration
            float s1 = (v2 - u2)/(2*a1);
            int t1 = (int)(1000*(2*s1)/(curVel + v));
            subMove(t1, NO_LIMIT, 0, curCnt + s1, 0, curVel, v, a1, 0, curTime, hold);
            return;
        }
        float vmax2 = a3*len + u2/2;
        if (vmax2 <= v2)
        {
            System.out.println("Triangle");
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
            subMove(t1, t2, t3, 0, s1+curCnt, curVel, v, a1, -a3, curTime, hold);         
        }
        else
        {
            System.out.println("Trap");
            // trapezoid move
            float s1 = (v2 - u2)/(2*a1);
            float s3 = (v2)/(2*a3);
            float s2 = len - s1 - s3;
            System.out.println("s1 " + s1 + " s2 " + s2 + " s3 " + s3);
            int t1 = (int)(1000*(2*s1)/(curVel + v));
            int t2 = t1 + (int)(1000*s2/v);
            int t3 = t2 + (int)(1000*(2*(s3))/(v));
            System.out.println("v " + v + " a1 " + a1 + " a3 " + (-a3));
            subMove(t1, t2, t3, curCnt+s1, curCnt+s1+s2, curVel, v, a1, -a3, curTime, hold);

        }
    }

    /**
     * Helper method, if move is currently active wait for it to be
     * completed
     */
    private void waitStop()
    {
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
        }
            
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
                genMove(curVelocity, curPosition, curCnt, curTime, 0, acceleration, NO_LIMIT, true);
                waitStop();
                updateVelocityAndPosition();
                genMove(curVelocity, curPosition, curCnt, 0, speed, acceleration, limit, hold);
            }
        }
        if (waitComplete)
            waitStop();
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
        return ibuf.get(port*6 + 3);
    }
    
    /**
     * Grabs the current state of the regulator
     */
    protected void updateVelocityAndPosition()
    {
        int baseCnt;
        int cnt;
        int vel;
        int time;
        // Check to make sure time is not changed during read
        do {
            time = ibuf.get(port*6 + 5);
            baseCnt = ibuf.get(port*6);
            cnt = ibuf.get(port*6+1);
            vel = ibuf.get(port*6+2);
        } while (time != ibuf.get(port*6 + 5));
        curCnt = FixToFloat(cnt);
        curPosition = curCnt + baseCnt;
        curVelocity = (FixToFloat(vel)/1024)*1000;
        curTime = time;
    }
    
    /**
     * returns the current position from the regulator
     * @return current position in degrees
     */
    public  float getPosition()
    {
        updateVelocityAndPosition();
        return curPosition;
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

    /**
     * Waits for the current move operation to complete
     */
    public void waitComplete()
    {
        waitStop();
    }
    
    protected int getState()
    {
        return ibuf.get(port*6 + 4);
    }
    
    public boolean isMoving()
    {
        return getState() >= ST_START;
    }
    
    public boolean isStalled()
    {
        return getState() == ST_STALL;
    }
    
    /**
     * Update the internal state of the motor.
     * @param velocity
     * @param hold
     * @param stalled
     */
    void updateState(int velocity, boolean hold, boolean stalled)
    {
        /*
            if (listener != null)
            {
                if (velocity == 0)
                    listener.rotationStopped(this, getTachoCount(), stalled, System.currentTimeMillis());
                else
                    listener.rotationStarted(this, getTachoCount(), false, System.currentTimeMillis());
            }
            */
    }
    
    /**
     *resets the tachometer count to 0;
     */ 
    public void resetTachoCount()
    {
        synchronized(cmd)
        {
            cmd[0] = OUTPUT_CLR_COUNT;
            cmd[1] = (byte)(1 << port);
            pwm.write(cmd,  2);
        }
    }
    
    public void setPWMMode(int mode)
    {
    }
    
    private static void initDeviceIO()
    {
        tacho = new NativeDevice("/dev/lms_motor");
        bbuf = tacho.mmap(96).getByteBuffer(0, 48);
        ibuf = bbuf.asIntBuffer();
        pwm = new NativeDevice("/dev/lms_pwm");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized MotorRegulator getRegulator()
    {
        if (regulator == null)
            regulator = new EV3MotorRegulatorKM(this);
        return regulator;
    }
}
