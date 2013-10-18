package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * Support for Dexter Industries DPressure500
 * Not tested.
 * 
 * See http://www.dexterindustries.com/Products-dPressure.html.
 * 
 * @author Lawrie Griffiths
 *
 */
public class DexterPressureSensor500 extends AnalogSensor implements SensorConstants, SampleProvider {
	/*
	 * TODO: Can 1023 really be right? Should it be 1024?
	 * 
	 * Formula from DPRESS-driver.h:
	 * vRef = 4.85
	 * vOut = rawValue * vRef / 1023
	 * result = (vOut / vRef - CAL1) / CAL2
	 */
	private static final double CAL1 = 0.04;
	private static final double CAL2 = 0.0018;
	
	/* 
	 * Optimized:
	 * result = rawValue * DPRESS_MULT - DPRESS_OFFSET;
	 */
	private static final float DPRESS_MULT = (float)(1.0 / CAL2 / 1023);
	private static final float DPRESS_OFFSET = (float)(CAL1 / CAL2);
	
    public DexterPressureSensor500(AnalogPort port) {
        super(port);
        port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
    public DexterPressureSensor500(Port port) {
        super(port);
        this.port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }
    
    /**
     * Return a sample provider for pressure mode.
     */
    public SampleProvider getPressureMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public int fetchSample(float[] sample, int offset) {
		sample[offset] = (port.readRawValue() * DPRESS_MULT - DPRESS_OFFSET) * 1000f; // in pacals
		return 0;
	}
}
