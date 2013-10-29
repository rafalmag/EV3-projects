package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Supports HiTechnics EOPD (Electro Optical Proximity Detector) sensor.<br>
 * This sensor is used to detect objects and small changes in distance to a target.
 *  Unlike the LEGO light sensor it is not affected by other light sources.
 *  
 *  See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NEO1048
 * 
 * @author Michael Smith <mdsmitty@gmail.com>
 * 
 */
public class HiTechnicEOPD extends AnalogSensor implements SensorConstants, SensorMode {

    protected static final long SWITCH_DELAY = 10;
    /**
     * By default the sensor is short range.
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (AnalogPort port){
        super(port);
        init();
    }
    
    /**
     * By default the sensor is short range.
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (Port port){
        super(port);
        init();
    }
		
    protected void init() {
    	setModes(new SensorMode[]{ this });
    	port.setTypeAndMode(TYPE_LIGHT_INACTIVE, MODE_RAW);
    }
    
	// TODO: Should we hae these set methods, or two sample modes?
	
	/**
	 * Changes the sensor to short range mode.
	 *
	 */
	public void setModeShort(){
	    switchType(TYPE_LIGHT_INACTIVE, SWITCH_DELAY);
	}
	
	/**
	 * Changes the port to long range mode.
	 *
	 */
	public void setModeLong(){
        switchType(TYPE_LIGHT_ACTIVE, SWITCH_DELAY);
	}
	
	/**
	 * Return a sample provider for processed distance value.
	 */
	public SensorMode getDistanceMode() {
		return this;
	}
	
	@Override
	public int sampleSize() {
		return 1;
	}
	
	@Override
	// TODO: What units is this in? Should we just return the raw value?
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = (float) Math.sqrt((1023-NXTRawValue(port.getPin1()))*10);
	}

	@Override
	public String getName() {
		return "Distance";
	}
}
