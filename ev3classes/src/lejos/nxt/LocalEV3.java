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
    public static final LocalEV3 ev3 = new LocalEV3();
    public final Battery battery = new LocalBattery();
    protected NXTRegulatedMotor [] motors = new NXTRegulatedMotor[MotorPort.PORTS];
    
    private LocalEV3()
    {        
    }
    
    public static EV3 getLocalEV3()
    {
        return ev3;
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
    
    /** {@inheritDoc}
     */    
    @Override
    public MotorPort openMotorPort(int port)
    {
        LocalMotorPort p = new LocalMotorPort();
        if (p != null && p.open(port))
            return p;
        return null;        
    }

    
    /** {@inheritDoc}
     */    
    @Override
    public MotorPort getMotorPort(int port)
    {
        MotorPort p = LocalMotorPort.getInstance(port);
        if (p != null)
            return p;
        return openMotorPort(port);    }

    
    /** {@inheritDoc}
     */    
    @Override
    public NXTRegulatedMotor openMotor(int port)
    {
        System.out.println("Open motor " + port);
        if (motors[port] != null)
            return null;
        motors[port] = new NXTRegulatedMotor(getMotorPort(port));
        return null;
    }

    
    /** {@inheritDoc}
     */    
    @Override
    public NXTRegulatedMotor getMotor(int port)
    {
        if (motors[port] != null)
            return motors[port];
        return openMotor(port);
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public Battery getBattery()
    {
        return battery;
    }
    
    // The following convenience methods try to duplicate the old NXT S1, S2 etc. but
    // in a form that is more compatible with the new sensor model.
    
    public static SensorPort S1()
    {
        return ev3.getSensorPort(0);
    }
    
    public static SensorPort S2()
    {
        return ev3.getSensorPort(1);
    }
    
    public static SensorPort S3()
    {
        return ev3.getSensorPort(2);
    }
    
    public static SensorPort S4()
    {
        return ev3.getSensorPort(3);
    }
    
    public static NXTRegulatedMotor A()
    {
        return ev3.getMotor(0);
    }
    
    public static NXTRegulatedMotor B()
    {
        return ev3.getMotor(1);
    }
    
    public static NXTRegulatedMotor C()
    {
        return ev3.getMotor(2);
    }
    
    public static NXTRegulatedMotor D()
    {
        return ev3.getMotor(3);
    }
    
    public static Battery battery()
    {
        return ev3.getBattery();
    }
}
