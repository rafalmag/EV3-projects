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

        return AnalogPort.getPortType(port);
    }
    
    public int getSensorType(int port)
    {
        return AnalogPort.getAnalogSensorType(port);
    }

    public void setPortMode(int port, int mode)
    {
        byte [] modes = new byte[PORTS];
        for(int i = 0; i < modes.length; i++)
            modes[i] = (byte)'-';
        modes[port] = (byte)mode;
        dev.write(modes, modes.length);
        
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
