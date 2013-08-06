package lejos.nxt;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;

import lejos.internal.io.NativeDevice;

public class DeviceManager implements EV3SensorConstants
{
    protected static final int ANALOG_SIZE = 5172;
    protected static final int ANALOG_INDCM_OFF = 5156;
    protected static final int ANALOG_INCONN_OFF = 5160;
    protected static NativeDevice dev;
    protected static Pointer pAnalog;
    protected static ByteBuffer inDcm;
    protected static ByteBuffer inConn;
    
    static {
        initDeviceIO();
    }

    public int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return inConn.get(port);
    }
    
    public String getPortTypeName(int typ)
    {
        switch(typ)
        {
        case CONN_INPUT_UART:
            return "UART";
        case CONN_UNKNOWN:
            return "Unknown";
        case CONN_INPUT_DUMB:
            return "Dumb";
        case CONN_NXT_DUMB:
            return "NXT Dumb";
        case CONN_NXT_IIC:
            return "NXT_IIC";
        case CONN_NXT_COLOR:
            return "NXT Color";
        case CONN_NONE:
            return "None";
        default:
            return "Error";
        }
    }
    
    
    private static void initDeviceIO()
    {
        dev = new NativeDevice("/dev/lms_analog"); 
        pAnalog = dev.mmap(ANALOG_SIZE);
        inDcm = pAnalog.getByteBuffer(ANALOG_INDCM_OFF, PORTS);
        inConn = pAnalog.getByteBuffer(ANALOG_INCONN_OFF, PORTS);
    }
}
