package lejos.nxt.addon;

import lejos.nxt.AnalogPort;
import lejos.nxt.SensorConstants;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Support for HiTechnic Magnetic sensor
 */
public class MagneticSensor implements SensorConstants {
    protected AnalogPort port;
	private int offset = 0;
	
    public MagneticSensor(AnalogPort port) {
		this.port = port;
		port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
	}
    
	public MagneticSensor(AnalogPort port, int offset) {
		this(port);
		this.offset = offset;
	}
	
	/**
	 * Get the relative magnetic field strength
	 * @return the relative magnetic field strength
	 */
	public int readValue() { 
		return (port.readRawValue() - offset); 
	}
}
	
	
