package lejos.hardware.sensor;
import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.SampleProvider;

/**
 * Sensor driver for the Lego EV3 Ultrasonic sensor.<br>
 * 
 * @author Aswin Bouwmeester
 *
 */
public class EV3UltrasonicSensor extends UARTSensor 
{
	
  /** 
   * Represent the Ultrasonic sensor in distance mode
   */
	private SampleProvider distanceMode;

	/** 
	 * Represent the Ultrasonic sensor in listen mode
   */
	private SampleProvider listenMode;

	private static final int DISABLED=3;
	private static final int SWITCHDELAY=200;
	
    /**
     * Create the Ultrasonic sensor class. 
     * @param port
     */
    public EV3UltrasonicSensor(Port port)
    {
        super(port,0);
    }
    
    /**
     * Create the Ultrasonic sensor class. 
     * @param port
     */
    public EV3UltrasonicSensor(UARTPort port)
    {
        super(port,0);
    }
    
    
    public SampleProvider getListenMode() {
    	if (listenMode==null) {
        listenMode=new ListenMode();
    	}
    	return listenMode;
    	
    }
    
    
    public SampleProvider getDistanceMode() {
    	if (distanceMode==null) {
        distanceMode=new DistanceMode();
    	}
    	return distanceMode;
    	
    }
    
    
    /**
     * Enable the sensor. This puts the indicater LED on.
     */
    public void enable() {
    	switchMode(0,SWITCHDELAY);
    }
    
    /**
     * Disable the sensor. This puts the indicater LED off.
     */
    public void disable() {
    	switchMode(DISABLED,SWITCHDELAY);
    }
    
    /** Indicate that the sensor is enabled.
     * @return
     * True, when the sensor is enabled. <br>
     * False, when the sensor is disabled.
     */
    public boolean isEnabled() {
    	return (currentMode == DISABLED) ? false: true;
    }
    
 
    
    /** 
     * Represents a Ultrasonic sensor in distance mode
     */
    private class DistanceMode implements SampleProvider {
    	private static final int MODE=0;
    	private static final float toSI=0.001f;

			@Override
			public int sampleSize() {
				return 1;
			}

			@Override
			public void fetchSample(float[] sample, int offset) {
				if (currentMode == DISABLED) return;
				switchMode(MODE,SWITCHDELAY);
				int raw=port.getShort();
				sample[offset]= (raw==2550) ? Float.POSITIVE_INFINITY : (float)raw*toSI;
			}
    	
    }
    
    /** 
     * Represents a Ultrasonic sensor in listen mode
     */
    private class ListenMode implements SampleProvider {
    	private static final int MODE=2;
    	
			@Override
			public int sampleSize() {
				return 1;
			}

			@Override
			public void fetchSample(float[] sample, int offset) {
				if (currentMode == DISABLED) return;
				switchMode(MODE,SWITCHDELAY);
				sample[offset]=port.getShort() & 0xff;
			}  	
    }
 
}
