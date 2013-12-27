package lejos.internal.ev3;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
    protected byte[] cmd = new byte[32];

    // Fixed point routines and constants
    static final int FIX_SCALE = 256;
    static final int FIX_SHIFT = 8;
    
    static int floatToFix(float f)
    {
        return Math.round(f*FIX_SCALE);
    }
    
    static int intToFix(int i)
    {
        return i << FIX_SHIFT;
    }
    
    static float FixToFloat(int fix)
    {
        return (float)fix/FIX_SCALE;
    }
    
    static int FixMult(int a, int b)
    {
        return (a*b) >> FIX_SHIFT;
    }
    
    static int FixDiv(int a, int b)
    {
        return (a << FIX_SHIFT)/b;
    }
    
    static int FixRound(int a)
    {
        return (a+FIX_SCALE/2) >> FIX_SHIFT;
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

    public void setControlParams(int typ, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD)
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
        pwm.write(cmd, 27);
        
    }
    
    /**
     * Helper method. Start a sub move operation. A sub move consists
     * of acceleration/deceleration to a set velocity and then holding that
     * velocity up to an optional limit point. If a limit point is set this
     * method will be called again to initiate a controlled deceleration
     * to that point
     * @param speed
     * @param acceleration
     * @param limit
     * @param hold
     */
    synchronized private void startSubMove(float speed, float acceleration, int limit, boolean hold)
    {
        // rescale speed and acceleration to be in units/1024mS rather than 1000mS
        speed = (speed/1000f)*1024f;
        acceleration = (acceleration/1000f)*1024f;
        // now start the actual move
        cmd[0] = OUTPUT_START;
        cmd[1] = (byte)port;
        setVal(cmd, 2, floatToFix(speed));
        setVal(cmd, 6, floatToFix(acceleration));
        setVal(cmd, 10, limit);
        cmd[15] = (byte) (hold ? 1 : 0);
        pwm.write(cmd,  15);
    }

    /**
     * Helper method, if move is currently active wait for it to be
     * completed
     */
    private void waitStop()
    {
        while(isMoving())
            Delay.msDelay(1);
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
        // Stop moves always happen now
        if (speed == 0)
            startSubMove(0, acceleration, NO_LIMIT, hold);
        else if (!isMoving())
        {
            // not moving so we start a new move
            startSubMove(speed, acceleration, limit, hold);
        }
        else
        {
            // TODO: Make this stuff possible
            // we already have a move in progress can we modify it to match
            // the new request? We must ensure that the new move is in the
            // same direction and that any stop will not exceed the current
            // acceleration request.
            /*
            float moveLen = limit - curCnt;
            float acc = (curVelocity*curVelocity)/(2*(moveLen));
            if (moveLen*curVelocity >= 0 && Math.abs(acc) <= acceleration)
                startSubMove(speed, acceleration, limit, hold);
            else
            {
                // Save the requested move
                newSpeed = speed;
                newAcceleration = acceleration;
                newLimit = limit;
                newHold = hold;
                pending = true;
                // stop the current move
                startSubMove(0, acceleration, NO_LIMIT, true);
                // If we need to wait for the existing command to end
                if (waitComplete)
                    waitStop();
            }*/
            startSubMove(0, acceleration, NO_LIMIT, true);
            waitStop();
            startSubMove(speed, acceleration, limit, hold);
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
        return ibuf.get(port*3 + 2);
    }
    
    public boolean isMoving()
    {
        return ibuf.get(port*3 + 1) != 0;
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
}
