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
public class AnalogPort implements EV3SensorConstants
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

    protected int port;
    
    /**
     * allow access to the specified port
     * @param p port number to open
     */
    public void open(int p)
    {
        this.port = p;
    }
    
    /**
     * Return the analog voltage reading from pin 1
     * @return the voltage in mV
     */
    public short getPin1()
    {
        return shortVals.getShort(ANALOG_PIN1_OFF + port*2);
    }
    
    /**
     * Return the analog voltage reading from pin 6
     * @return the voltage in mV
     */
   public short getPin6()
    {
        return shortVals.getShort(ANALOG_PIN6_OFF + port*2);
    }
    
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
    public static short getBatteryTemperature()
    {
        return shortVals.getShort(ANALOG_BAT_TEMP_OFF);
    }

    /**
     * return the motor current
     * @return
     */
    public static short getMotorCurrent()
    {
        return shortVals.getShort(ANALOG_MOTOR_CUR_OFF);
    }

    /**
     * return the battery current
     * @return
     */
    public static short getBatteryCurrent()
    {
        return shortVals.getShort(ANALOG_BAT_CUR_OFF);
    }

    /**
     * return the battery voltage
     * @return
     */
    public static short getBatteryVoltage()
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
