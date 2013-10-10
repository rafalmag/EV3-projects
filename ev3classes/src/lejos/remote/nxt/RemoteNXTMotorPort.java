package lejos.remote.nxt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.internal.io.NativeDevice;

/**
 * 
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public class RemoteNXTMotorPort extends RemoteNXTIOPort implements TachoMotorPort {
    public RemoteNXTMotorPort(NXTCommand nxtCommand) {
		super(nxtCommand);
	}

	static final byte OUTPUT_POWER = (byte)0xa4;
    static final byte OUTPUT_START = (byte)0xa6;
    static final byte OUTPUT_STOP = (byte)0xa3;
    static final byte OUTPUT_CLR_COUNT = (byte)0xb2;
    protected static NativeDevice tacho;
    protected static ByteBuffer bbuf;
    protected static IntBuffer ibuf;
    protected static NativeDevice pwm;
    static
    {
        initDeviceIO();
    }
    protected int curMode = FLOAT+1; // current mode is unknown
    protected byte[] cmd = new byte[8];
    
   
    

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
        // Convert lejos power and mode to NXT power and mode
        if (mode >= STOP)
            power = 0;
        else if (mode == 2)
            power = -power;
        synchronized(cmd)
        {
            cmd[0] = OUTPUT_POWER;
            cmd[1] = (byte)(1 << port);
            cmd[2] = (byte)power;
            pwm.write(cmd,  3);
            if (mode != curMode)
            {
                if (mode <= BACKWARD)
                {
                    if (curMode >= STOP)
                    {
                        // motor not running start it
                        cmd[0] = OUTPUT_START;
                        pwm.write(cmd,  2);
                    }
                }
                else
                {
                    if (mode == FLOAT)
                    {
                        // need to stop the motor
                        cmd[0] = OUTPUT_STOP;
                        // Set float
                        cmd[2] = (byte)0;
                        pwm.write(cmd, 3);
                    }
                    else
                    {
                        // STOP leave motor powered up but with zero power
                        cmd[0] = OUTPUT_START;
                        pwm.write(cmd,  2);
                    }
                }
                curMode = mode;
            }
        }
    }


    /**
     * returns tachometer count
     */
    public  int getTachoCount()
    {
        return ibuf.get(port*3 + 2);
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
