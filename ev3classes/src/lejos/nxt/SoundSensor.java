package lejos.nxt;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Abstraction for a NXT sound sensor.
 * 
 */
public class SoundSensor extends AnalogSensor implements SensorConstants {
	
	/**
	 * Create a sound sensor object attached to the specified open port,
	 * and sets DB or DBA mode.
	 * @param port open analog sensor port.
     * @param dba true to set DBA mode, false for DB mode.
	 */
	public SoundSensor(AnalogPort port, boolean dba)
	{
	    super(port);
	    this.port.setTypeAndMode(
               (dba ? TYPE_SOUND_DBA
                    : TYPE_SOUND_DB),
               MODE_PCTFULLSCALE);   
	}

	/**
	 * Create a sound sensor. The sensor will be set to operate in DB mode
	 * @param port the sensor port to use
	 */
	public SoundSensor(Port port)
	{
	    this(port, false);
	}
	/**
	 * Create a sound sensor object attached to the specified port,
	 * and sets DB or DBA mode.
	 * @param port port, e.g. Port.S1
	 * @param dba true to set DBA mode, false for DB mode.
	 */
	public SoundSensor(Port port, boolean dba)
	{
	   super(port);
       this.port.setTypeAndMode(
    		   (dba ? TYPE_SOUND_DBA
    				: TYPE_SOUND_DB),
    		   MODE_PCTFULLSCALE);   
	}
	
	/**
	 * Set DB or DBA mode.
	 * @param dba true to set DBA mode, false for DB mode.
	 */
	public void setDBA(boolean dba)
	{
	    port.setType((dba ? TYPE_SOUND_DBA
	    				  : TYPE_SOUND_DB));
	}

	/**
	 * Read the current sensor value.
	 * @return value as a percentage.
	 */
	public int readValue()
	{
		return ((1023 - port.readRawValue()) * 100/ 1023);  
	}
}
