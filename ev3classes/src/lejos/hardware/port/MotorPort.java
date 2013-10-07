package lejos.hardware.port;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import lejos.hardware.ev3.LocalEV3;
import lejos.internal.io.NativeDevice;

/**
 * 
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public interface MotorPort {
	
    
	/**
	 * MotorPort A.
	 */
	public static final Port A = LocalEV3.ev3.getPort("A");
	
	/**
	 * MotorPort B.
	 */
	public static final Port B = LocalEV3.ev3.getPort("B");
	
    /**
     * MotorPort C.
     */
    public static final Port C = LocalEV3.ev3.getPort("C");
    
    /**
     * MotorPort D.
     */
    public static final Port D = LocalEV3.ev3.getPort("D");

}
