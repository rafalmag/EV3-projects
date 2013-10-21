package lejos.remote.nxt;

import java.io.IOException;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.LegacySensorPort;
import lejos.hardware.port.PortException;

/**
 * This class provides access to the EV3 Analog based sensor ports and other
 * analog data sources.
 * 
 * @author Lawrie Griffiths
 *
 */
public class RemoteNXTAnalogPort extends RemoteNXTIOPort implements AnalogPort, LegacySensorPort
{
	public RemoteNXTAnalogPort(NXTCommand nxtCommand) {
		super(nxtCommand);
	}

	private int id;
	private int type, mode;

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
		} catch (IOException e) {
			throw new PortException(e);
		}
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
		} catch (IOException e) {
			throw new PortException(e);
		}
    }

    @Override
    public int readRawValue()
    {
		try {
			InputValues vals = nxtCommand.getInputValues(id);
			return vals.rawADValue;
		} catch (IOException e) {
			throw new PortException(e);
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
	public void activate() {
		// TODO: How can we support this for a remote NXT?
	}

	@Override
	public void passivate() {
		// TODO: How can we support this for a remote NXT?	
	}

	@Override
	public void getShorts(short[] vals, int offset, int length) {
		throw new UnsupportedOperationException("Not supported for a remote NXT");
	}

	@Override
	public int getPin6() {
		throw new UnsupportedOperationException("Not supported for a remote NXT");
	}

	@Override
	public int getPin1() {
		throw new UnsupportedOperationException("Not supported for a remote NXT");
	}
}
