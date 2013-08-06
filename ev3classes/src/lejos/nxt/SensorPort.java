package lejos.nxt;


import lejos.nxt.remote.*;
import java.io.*;

/**
 * Port class. Contains 4 Port instances.<br>
 * Usage: Port.S4.readValue();
 * 
 * This version of the SensorPort class supports remote execution.
 * 
 * @author <a href="mailto:bbagnall@mts.net">Brian Bagnall</a>
 *
 */
public class SensorPort implements NXTProtocol, LegacySensorPort, I2CPort  {	
	
	private int id;
	
	public static SensorPort S1 = new SensorPort(0);
	public static SensorPort S2 = new SensorPort(1);
	public static SensorPort S3 = new SensorPort(2);
	public static SensorPort S4 = new SensorPort(3);
	
	private static SensorPort[] ports = {S1,S2,S3,S4};
	
	
	private SensorPort(int port) {
		id = port;
	}
	
	public int getId() {
		return id;
	}
	
	public static SensorPort getInstance(int port) {
		return ports[port];
	}
	
	public void setTypeAndMode(int type, int mode) {
	}
	
	public void setType(int type) {
		int mode = getMode();
	}
	
	public void setMode(int mode) {
		int type = getType();
	}
	
	public int getType() {
		InputValues vals;
		//return vals.sensorType;
		return -1;
	}
	
	public int getMode() {
		InputValues vals;
		//return vals.sensorMode;
		return 0;
	}
	
	/**
	 * Reads the boolean value of the sensor.
	 * @return Boolean value of sensor.
	 */
	public boolean readBooleanValue() {
		InputValues vals;
		return false;
	}
	
    /**
     * Reads the raw value of the sensor.
     * @return Raw sensor value. Range is device dependent.
     */
	public int readRawValue() {
		InputValues vals;
		//return vals.rawADValue;
		return 0;
	}
    
	/**
	 * Reads the normalized value of the sensor.
	 * @return Normalized value. 0 to 1023
	 */
	public int readNormalizedValue() {
		InputValues vals;
		//return vals.normalizedADValue;
		return 0;
	}
	
	/**
	 * Returns scaled value, depending on mode of sensor. 
	 * e.g. BOOLEANMODE returns 0 or 1.
	 * e.g. PCTFULLSCALE returns 0 to 100.
	 * @return the value
	 * @see SensorPort#setTypeAndMode(int, int)
	 */
	public int readValue() {
		InputValues vals;
		//return vals.scaledValue;
		return 0;
	}
	
	/**
	 * Activate an RCX Light Sensor
	 */
	public void activate() {
		setType(REFLECTION);
	}
	
	/**
	 * Passivate an RCX Light Sensor
	 */
	public void passivate() {
		setType(NO_SENSOR);
	}

    /**
     * Return a variable number of sensor values.
     * NOTE: Currently there is no way to return multiple results from a
     * remote sensor, so we return an error.
     * @param values An array in which to return the sensor values.
     * @return The number of values returned.
     */
    public int readValues(int[] values)
    {
        return -1;
    }

    /**
     * Return a variable number of raw sensor values
     * NOTE: Currently there is no way to return multiple results from a
     * remote sensor, so we return an error.
     * @param values An array in which to return the sensor values.
     * @return The number of values returned.
     */
    public int readRawValues(int[] values)
    {
        return -1;
    }

    public void enableColorSensor()
    {

    }

}
