package lejos.nxt;

import java.io.*;
import lejos.nxt.remote.*;

/**
 * Abstraction for the local NXT device.
 * 
 * This version of the NXT class supports remote execution.
 * 
 * @author Lawrie Griffiths and Brian Bagnall
 *
 */
public class NXT {

	/**
	 * Get the (emulated) standard LEGO firmware version number
	 * 
	 * @return the version number
	 */
	public static float getFirmwareVersion() {
	    return 1.0f;
	}

	/**
	 * Get the LEGO Communication Protocol version number 
	 * 
	 * @return the version number
	 */
	public static float getProtocolVersion() {
	    return 1.0f;
	}
	
	/**
	 * Get the number of bytes of free flash memory
	 * @return Free memory remaining in FLASH
	 */
	public static int getFlashMemory() {
	    return 0;
	}
	
	/**
	 * Deletes all user programs and data in FLASH memory
	 * @return the status
	 */
	public static byte deleteFlashMemory() {
	    return -1;
	}
	
	/**
	 * Get the friendly name of the brick
	 * 
	 * @return the friendly name
	 */
	public static String getBrickName() {
	    return "EV3";
	}

	/**
	 * Set the friendly name of the brick
	 * 
	 * @return the status code
	 */
	public static byte setBrickName(String newName) {
		return -1;
	}
	
	/**
	 * This doesn't seem to be implemented in Lego NXT firmware/protocol?
	 * @return Seems to return 0 every time
	 */
	public static int getSignalStrength() {
	    return -1;
	}
	
	/**
	 * Close the connection to the NXT and exit
	 * 
	 * @param code the exit code
	 */
	public static void exit(int code) {
		System.exit(code);
	}
}
