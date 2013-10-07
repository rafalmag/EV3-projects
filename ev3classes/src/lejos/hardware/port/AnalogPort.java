package lejos.hardware.port;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * An abstraction for a port that supports Analog/Digital sensors.
 * 
 * @author Lawrie Griffiths.
 * 
 */
public interface AnalogPort extends IOPort, BasicSensorPort {

    /**
     * Return a boolean value based upon an analog reading 
     * @deprecated This method is provided for compatibility with NXT sensors. For new drivers
     * please consider using other read methods.
     * @return true/false depending upon the port value
     */
    @Deprecated
    public boolean readBooleanValue();
	
    /**
     * Return a raw analog reading 
     * @deprecated This method is provided for compatibility with NXT sensors. For new drivers
     * please consider using other read methods.
     * @return raw port value 0-1023
     */
    @Deprecated
	public int readRawValue();
	
    /**
     * Return an analog reading 
     * @deprecated This method is provided for compatibility with NXT sensors. For new drivers
     * please consider using other read methods.
     * @return port value 0-1023
     */
    @Deprecated
	public int readValue();

    /**
     * return the analog value on pin 6 of the sensor port
     * @return analog value 0-4095
     */
	public int getPin6();
	
    /**
     * return the analog value on pin 1 of the sensor port
     * @return analog value 0-4095
     */
	public int getPin1();
}
