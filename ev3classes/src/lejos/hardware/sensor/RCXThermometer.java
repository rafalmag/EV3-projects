package lejos.hardware.sensor;

import lejos.hardware.port.LegacySensorPort;
import lejos.robotics.SampleProvider;

/** 
 *Abstraction for an RCX temperature sensor. 
 * 
 * @author Soren Hilmer
 * 
 */
public class RCXThermometer implements SensorConstants, SampleProvider {
    LegacySensorPort port;
    
    /**
     * Create an RCX temperature sensor object attached to the specified port.
     * @param port port, e.g. Port.S1
     */
    public RCXThermometer(LegacySensorPort port)
    {
        this.port = port;
        port.setTypeAndMode(TYPE_TEMPERATURE,
                            MODE_RAW);
    }
    
    /**
     * Return a sample provider in temperature mode
     */
    public SampleProvider getTemperatureMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = (785-port.readRawValue())/8.0f +273.15f; // Kelvin
	}

}
