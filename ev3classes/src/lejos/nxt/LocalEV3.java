package lejos.nxt;

/**
 * This class represents the local instance of an EV3 device. It can be used to
 * obtain access to the various system resources (Sensors, Motors etc.).
 * @author andy
 *
 */
public class LocalEV3 implements EV3
{
    protected DeviceManager ldm = DeviceManager.getLocalDeviceManager();
    protected static EV3 localEV3 = new LocalEV3();

    private LocalEV3()
    {        
    }
    
    public static EV3 getLocalEV3()
    {
        return localEV3;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public synchronized SensorPort newSensorPort(int typ)
    {
        //System.out.println("get port type " + typ);
        switch (typ)
        {
        case SensorPort.CONN_NXT_DUMB:
        case SensorPort.CONN_INPUT_DUMB:
        case SensorPort.CONN_NXT_COLOR:
            return new LocalAnalogPort();
        case SensorPort.CONN_INPUT_UART:
            return new LocalUARTPort();
        case SensorPort.CONN_NXT_IIC:
            return new LocalI2CPort();
        }
        return null;
    }
    
    /** {@inheritDoc}
     */        
    @Override
    public SensorPort openSensorPort(int port)
    {
        return openSensorPort(port, ldm.getPortType(port));
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public synchronized SensorPort openSensorPort(int port, int typ)
    {
        //System.out.println("open port " + port + " type " + typ);
        SensorPort p = newSensorPort(typ);
        if (p != null && p.open(port))
            return p;
        return null;        
    }
    /** {@inheritDoc}
     */    
    @Override
    public synchronized SensorPort getSensorPort(int port)
    {
        SensorPort sp = LocalSensorPort.getInstance(port);
        if (sp != null)
            return sp;
        return openSensorPort(port);
    }
    
    // The following convenience methods try to duplicate the old NXT S1, S2 etc. but
    // in a form that is more compatible with the new sensor model.
    
    public static SensorPort S1()
    {
        return localEV3.getSensorPort(0);
    }
    
    public static SensorPort S2()
    {
        return localEV3.getSensorPort(1);
    }
    
    public static SensorPort S3()
    {
        return localEV3.getSensorPort(2);
    }
    
    public static SensorPort S4()
    {
        return localEV3.getSensorPort(3);
    }
}
