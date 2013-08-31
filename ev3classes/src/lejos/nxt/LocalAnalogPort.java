package lejos.nxt;

import java.nio.ByteBuffer;

import lejos.internal.io.NativeDevice;

import com.sun.jna.Pointer;

/**
 * This class provides access to the EV3 Analog based sensor ports and other
 * analog data sources.
 * 
 * @author andy
 *
 */
public class LocalAnalogPort extends LocalSensorPort implements ADSensorPort
{
    protected static final int ANALOG_SIZE = 5172;
    protected static final int ANALOG_PIN1_OFF = 0;
    protected static final int ANALOG_PIN6_OFF = 8;
    protected static final int ANALOG_PIN5_OFF = 16;
    protected static final int ANALOG_BAT_TEMP_OFF = 24;
    protected static final int ANALOG_MOTOR_CUR_OFF = 26;
    protected static final int ANALOG_BAT_CUR_OFF = 28;
    protected static final int ANALOG_BAT_V_OFF = 30;
    protected static final int ANALOG_INDCM_OFF = 5156;
    protected static final int ANALOG_INCONN_OFF = 5160;
    protected static NativeDevice dev;
    protected static Pointer pAnalog;
    protected static ByteBuffer inDcm;
    protected static ByteBuffer inConn;
    protected static ByteBuffer shortVals;
    
    static {
        initDeviceIO();
    }

    
    /**
     * allow access to the specified port
     * @param p port number to open
     */
    public boolean open(int p)
    {
        return super.open(p);
    }
    
    /**
     * Return the analog voltage reading from pin 1
     * @return the voltage in mV
     */
    public int getPin1()
    {
        return shortVals.getShort(ANALOG_PIN1_OFF + port*2);
    }
    
    /**
     * Return the analog voltage reading from pin 6
     * @return the voltage in mV
     */
    public int getPin6()
    {
        return shortVals.getShort(ANALOG_PIN6_OFF + port*2);
    }

    // The following method provide compatibility with NXT sensors
    
    @Override
    public boolean setType(int type)
    {
        switch(type)
        {
        case TYPE_NO_SENSOR:
        case TYPE_SWITCH:
        case TYPE_TEMPERATURE:
        case TYPE_CUSTOM:
        case TYPE_ANGLE:
            setPinMode(CMD_FLOAT);
            break;
        case TYPE_LIGHT_ACTIVE:
        case TYPE_SOUND_DBA:            
        case TYPE_REFLECTION:
            setPinMode(CMD_SET|CMD_PIN5);
            break;
        case TYPE_LIGHT_INACTIVE:
        case TYPE_SOUND_DB: 
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED:
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED_9V:
            setPinMode(CMD_SET|CMD_PIN1);
            break;
        default:
            throw new UnsupportedOperationException("Unrecognised sensor type");
        }
        return true;
    }

    
    @Override
    public boolean readBooleanValue()
    {
        return getPin1() < 2400;
    }

    @Override
    public int readRawValue()
    {
        // TODO Auto-generated method stub
        return (getPin1() + 3)/4;
    }

    @Override
    public int readValue()
    {
        // TODO Auto-generated method stub
        return (getPin1() + 3)/4;
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
        return shortVals.getShort(ANALOG_PIN5_OFF + p*2);
    }

    /**
     * Return the battery temperature reading
     * @return
     */
    protected static short getBatteryTemperature()
    {
        return shortVals.getShort(ANALOG_BAT_TEMP_OFF);
    }

    /**
     * return the motor current
     * @return
     */
    protected static short getMotorCurrent()
    {
        return shortVals.getShort(ANALOG_MOTOR_CUR_OFF);
    }

    /**
     * return the battery current
     * @return
     */
    protected static short getBatteryCurrent()
    {
        return shortVals.getShort(ANALOG_BAT_CUR_OFF);
    }

    /**
     * return the battery voltage
     * @return
     */
    protected static short getBatteryVoltage()
    {
        return shortVals.getShort(ANALOG_BAT_V_OFF);
    }

    /**
     * get the type of the port
     * @param port
     * @return
     */
    protected static int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return inConn.get(port);
    }

    /**
     * Get the type of analog sensor (if any) attached to the port
     * @param port
     * @return
     */
    protected static int getAnalogSensorType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return inDcm.get(port);
    }
        
    
    private static void initDeviceIO()
    {
        dev = new NativeDevice("/dev/lms_analog"); 
        pAnalog = dev.mmap(ANALOG_SIZE);
        inDcm = pAnalog.getByteBuffer(ANALOG_INDCM_OFF, PORTS);
        inConn = pAnalog.getByteBuffer(ANALOG_INCONN_OFF, PORTS);
        shortVals = pAnalog.getByteBuffer(0, ANALOG_BAT_V_OFF+2);
    }

}
