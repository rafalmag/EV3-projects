package lejos.nxt.addon;

import lejos.nxt.AnalogPort;
import lejos.nxt.AnalogSensor;
import lejos.nxt.Port;
import lejos.nxt.SensorConstants;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Support for HiTechnic Magnetic sensor
 */
public class MagneticSensor extends AnalogSensor implements SensorConstants {
	private int offset = 0;
	
    public MagneticSensor(AnalogPort port) {
        super(port);
        //port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
    public MagneticSensor(AnalogPort port, int offset) {
        this(port);
        this.offset = offset;
    }
    
    public MagneticSensor(Port port) {
        super(port);
        this.port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
    public MagneticSensor(Port port, int offset) {
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
	
	
