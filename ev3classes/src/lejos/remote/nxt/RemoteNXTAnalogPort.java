package lejos.remote.nxt;

import java.io.IOException;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.LegacySensorPort;

/**
 * This class provides access to the EV3 Analog based sensor ports and other
 * analog data sources.
 * 
 * @author andy
 *
 */
public class RemoteNXTAnalogPort extends RemoteNXTIOPort implements AnalogPort, LegacySensorPort
{
	public RemoteNXTAnalogPort(NXTCommand nxtCommand) {
		super(nxtCommand);
	}

	private int id;
	private int type, mode;
	
    /**
     * Return the analog voltage reading from pin 1
     * @return the voltage in mV
     */
    public int getPin1()
    {
        return -1;
    }
    
    /**
     * Return the analog voltage reading from pin 6
     * @return the voltage in mV
     */
    public int getPin6()
    {
        return -1;
    }

    // The following method provide compatibility with NXT sensors
    
	/**
	 * Set the sensor type and mode
	 * @param type the sensor type
	 * @param mode the sensor mode
	 * @return 
	 */
	public boolean setTypeAndMode(int type, int mode) {
		this.type = type;
		this.mode = mode;
		try {
			nxtCommand.setInputMode(id, type, mode);
		} catch (IOException ioe) {}
		return true;
	}
	
	/**
	 * Set the sensor type
	 * @param type the sensor type
	 * @return 
	 */
	public boolean setType(int type) {
		this.type = type;
		setTypeAndMode(type, mode);
		return true;
	}
	
	/**
	 * Set the sensor mode
	 * @param mode the sensor mode
	 * @return 
	 */
	public boolean setMode(int mode) {
		this.mode = mode;
		setTypeAndMode(type, mode);
		return true;
	}
	
    @Override
    public boolean readBooleanValue()
    {
		try {
			InputValues vals = nxtCommand.getInputValues(id);
			return (vals.rawADValue<600);			
		} catch (IOException ioe) {
			return false;
		}
    }

    @Override
    public int readRawValue()
    {
		try {
			InputValues vals = nxtCommand.getInputValues(id);
			return vals.rawADValue;
		} catch (IOException ioe) {
			return 0;
		}
    }

    @Override
    public int readValue()
    {
	    int rawValue = readRawValue();
	    
	    if (mode == MODE_BOOLEAN)
	    {
	    	return (rawValue < 600 ? 1 : 0);
	    }
	    
	    if (mode == MODE_PCTFULLSCALE)
	    {
	    	return ((1023 - rawValue) * 100/ 1023);
	    }
	    
	    return rawValue;
    }

    // The following methods should arguably be in different class, but they
    // share the same memory structures as those used for analog I/O. Perhaps
    // we need to restructure at some point and mode them to a common private
    // class.
    
    /**
     * Return the analog voltage reading from pin 5 on the output port
     * @param p the port number to return the reading for
     * @return the voltage in mV
     */
    protected static short getOutputPin5(int p)
    {
        return -1;
    }

    /**
     * Return the battery temperature reading
     * @return
     */
    protected static short getBatteryTemperature()
    {
        return -1;
    }

    /**
     * return the motor current
     * @return
     */
    protected static short getMotorCurrent()
    {
        return -1;
    }

    /**
     * return the battery current
     * @return
     */
    protected static short getBatteryCurrent()
    {
        return -1;
    }

    /**
     * return the battery voltage
     * @return
     */
    protected static short getBatteryVoltage()
    {
        return -1;
    }

    /**
     * get the type of the port
     * @param port
     * @return
     */
    public static int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return 0;
    }

    /**
     * Get the type of analog sensor (if any) attached to the port
     * @param port
     * @return
     */
    public static int getAnalogSensorType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return 0;
    }

    @Override
    public void getShorts(short[] vals, int offset, int length)
    {
    }

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivate() {
		// TODO Auto-generated method stub
		
	}
}
