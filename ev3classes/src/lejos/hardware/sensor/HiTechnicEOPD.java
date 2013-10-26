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
	
    /**
     * By default the sensor is short range.
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (AnalogPort port){
        super(port);
        init(false);
    }
    
    /**
     * By default the sensor is short range.
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (Port port){
        super(port);
        init(false);
    }
	
	/**
	 * 
	 * @param port NXT sensor port 1-4.
	 * @param longRange true = long range false = short range.
	 */
	public HiTechnicEOPD(Port port, boolean longRange)
	{
	    super(port);
		init(longRange);
	}
	
    protected void init(boolean longRange) {
    	setModes(new SensorMode[]{ this });
    	port.setTypeAndMode((longRange ? TYPE_LIGHT_ACTIVE : TYPE_LIGHT_INACTIVE),MODE_PCTFULLSCALE);
    }
    
	// TODO: Should we hae these set methods, or two sample modes?
	
	/**
	 * Changes the sensor to short range mode.
	 *
	 */
	public void setModeShort(){
		port.setTypeAndMode(TYPE_LIGHT_INACTIVE, MODE_PCTFULLSCALE);
	}
	
	/**
	 * Changes the port to long range mode.
	 *
	 */
	public void setModeLong(){
		port.setTypeAndMode(TYPE_LIGHT_ACTIVE, MODE_PCTFULLSCALE);
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
		sample[offset] = (float) Math.sqrt((1023-port.readRawValue())*10);
	}

	@Override
	public String getName() {
		return "Distance";
	}
}
