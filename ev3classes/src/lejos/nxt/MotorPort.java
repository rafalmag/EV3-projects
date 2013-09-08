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
public interface MotorPort extends TachoMotorPort {
	
    /**
     * The number of ports available.
     */
    public static final int PORTS = 4;
    
	/**
	 * MotorPort A.
	 */
	public static final MotorPort A = LocalEV3.ev3.getMotorPort(0);
	
	/**
	 * MotorPort B.
	 */
	public static final MotorPort B = LocalEV3.ev3.getMotorPort(1);
	
    /**
     * MotorPort C.
     */
    public static final MotorPort C = LocalEV3.ev3.getMotorPort(2);
    
    /**
     * MotorPort D.
     */
    public static final MotorPort D = LocalEV3.ev3.getMotorPort(3);
    
	
    /**
     * Open the port and make it available for use.
     * @param port the port number to open
     */    
    public boolean open(int port);
    
    /**
     * Close the port, the port can not be used after this call.
     */
    public void close();
    
    /**
     * Return the id/port number for this port.
     * @return
     */
    public int getId();
}
