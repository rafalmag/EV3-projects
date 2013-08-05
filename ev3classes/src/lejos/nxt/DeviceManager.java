package lejos.nxt;

import lejos.internal.io.NativeDevice;

public class DeviceManager implements EV3SensorConstants
{
    protected static NativeDevice dev;
    
    static {
        initDeviceIO();
    }

    public int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        byte[] types = new byte[PORTS*2 + 2];
        dev.read(types, types.length);
        if (types[port] == 125)
            return CONN_INPUT_UART;
        else
            return CONN_NONE;
        //return types[port];
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
        dev = new NativeDevice("/dev/lms_dcm");        
    }
}
