package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * Support for HiTechnic Magnetic sensor
 * 
 * See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NMS1035
 * 
 * @author Lawrie Griffiths
 * 
 */
public class HiTechnicMagneticSensor extends AnalogSensor implements SensorConstants, SampleProvider {;
	
	/**
	 * Create a magnetic sensor on an analog port
	 * 
	 * @param port the analog port
	 */
    public HiTechnicMagneticSensor(AnalogPort port) {
        super(port);
        port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
	/**
	 * Create a magnetic sensor on a sensor port
	 * 
	 * @param port the analog port
	 */
    public HiTechnicMagneticSensor(Port port) {
        super(port);
        this.port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
    /**
     * Return a sample provider in magnetic mode
     */
    public SampleProvider getMagneticMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = port.readRawValue();
	}
}
	
	
