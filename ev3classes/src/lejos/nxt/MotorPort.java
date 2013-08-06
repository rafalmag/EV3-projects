package lejos.nxt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.internal.io.NativeDevice;

/**
 * 
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public class MotorPort implements TachoMotorPort {
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
	
	private int _id;
	private int _pwmMode = PWM_FLOAT; // default to float mode
	private int curMode = 3;
	private byte[] cmd = new byte[8];
	
	private MotorPort(int id)
	{
		_id = id;
	}
	
    /**
     * The number of ports available.
     */
    public static final int NUMBER_OF_PORTS = 4;
    
	/**
	 * MotorPort A.
	 */
	public static final MotorPort A = new MotorPort (0);
	
	/**
	 * MotorPort B.
	 */
	public static final MotorPort B = new MotorPort (1);
	
    /**
     * MotorPort C.
     */
    public static final MotorPort C = new MotorPort (2);
    
    /**
     * MotorPort D.
     */
    public static final MotorPort D = new MotorPort (3);
    
    /**
     * Return the MotorPort with the given Id.
     * @param id the Id, between 0 and {@link #NUMBER_OF_PORTS}-1.
     * @return the MotorPort object
     */
    public static MotorPort getInstance(int id)
    {
    	switch (id)
    	{
    		case 0:
    			return MotorPort.A;
    		case 1:
    			return MotorPort.B;
            case 2:
                return MotorPort.C;
            case 3:
                return MotorPort.D;
    		default:
    			throw new IllegalArgumentException("no such motor port");
    	}
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
		// Convert lejos power and mode to NXT power and mode
	    if (mode >= 3)
	        power = 0;
	    else if (mode == 2)
	        power = -power;
	    synchronized(cmd)
	    {
	        cmd[0] = OUTPUT_POWER;
	        cmd[1] = (byte)(1 << _id);
	        cmd[2] = (byte)power;
	        pwm.write(cmd,  3);
	        if (mode != curMode)
	        {
	            if (mode <= 2)
	            {
	                if (curMode >= 3)
	                {
	                    // motor not running start it
	                    cmd[0] = OUTPUT_START;
	                    pwm.write(cmd,  2);
	                }
	            }
	            else
	            {
	                // need to stop the motor
	                cmd[0] = OUTPUT_STOP;
	                // Set brake or float mode
	                cmd[2] = (byte)(mode == 3 ? 1 : 0);
	                pwm.write(cmd, 3);
	            }
	        }
	    }
	}


	/**
	 * returns tachometer count
	 */
	public  int getTachoCount()
	{
		return ibuf.get(_id*3 + 2);
	}
	
    /**
	 *resets the tachometer count to 0;
	 */ 
	public void resetTachoCount()
	{
		synchronized(cmd)
		{
		    cmd[0] = OUTPUT_CLR_COUNT;
		    cmd[1] = (byte)(1 << _id);
		    pwm.write(cmd,  2);
		}
	}
	
	public void setPWMMode(int mode)
	{
		_pwmMode = mode;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	private static void initDeviceIO()
	{
        tacho = new NativeDevice("/dev/lms_motor");
        bbuf = tacho.mmap(96).getByteBuffer(0, 48);
        ibuf = bbuf.asIntBuffer();
        pwm = new NativeDevice("/dev/lms_pwm");

	}
}
