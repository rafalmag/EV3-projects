package lejos.nxt;

/**
 * This class provides the base operations for local EV3 sensor ports.
 * @author andy
 *
 */
public abstract class LocalSensorPort implements SensorPort
{
    protected int port = -1;
    protected static byte [] dc = new byte[3*PORTS];
    protected int mode = 0;
    protected static LocalSensorPort [] openPorts = new LocalSensorPort[PORTS];
   

    @Override
    public int getId()
    {
        return port;
    }
    
    @Override
    public int getMode()
    {
        return mode;
    }

    @Override
    public int getType()
    {
        return 0;
    }

    @Override
    public boolean setMode(int mode)
    {
        this.mode = mode;
        return true;
    }

    @Override
    public boolean setType(int type)
    {
        throw new UnsupportedOperationException("This operation is for legacy modes only");

    }

    @Override
    public boolean setTypeAndMode(int type, int mode)
    {
        setType(type);
        setMode(mode);
        return true;
    }
   
    /**
     * Open the port and make it available for use.
     * @param port the port number to open
     * @return true if the port is now open, false if there is an error
     */
    public boolean open(int port)
    {
        synchronized (openPorts)
        {
            if (openPorts[port] == null)
            {
                openPorts[port] = this;
                this.port = port;
                return true;
            }
            return false;
        }
    }
   
    /**
     * Close the port, the port can not be used after this call.
     */
    public void close()
    {
        if (port == -1)
            throw new IllegalStateException("Port is not open");
        synchronized (openPorts)
        {
            openPorts[port] = null;
            port = -1;
        }
    }
    
    public static SensorPort getInstance(int port)
    {
        return openPorts[port];
    }
    
    /**
     * Create and return a devCon structure ready for use. Note that this structure
     * when used will impact all of the UART ports currently active. Thus the values
     * used for other ports in earlier operations must be preserved.
     * @param p port number
     * @param conn connection type
     * @param typ sensor type
     * @param mode operating mode
     * @return the DEVCON structure ready for use
     */
    protected synchronized static byte[] devCon(int p, int conn, int typ, int mode)
    {
        // structure is 3 byte arrays
        //byte [] dc = new byte[3*PORTS];
        dc[p] = (byte)conn;
        dc[p + PORTS] = (byte) typ;
        dc[p + 2*PORTS] = (byte) mode;
        return dc;
    }
    
   /**
     * Set the port pins up ready for use.
     * @param mode The EV3 pin mode
     */
    public void setPinMode(int mode)
    {
        //System.out.println("Set Pin mode port " + port + " value " + mode);
        DeviceManager.getLocalDeviceManager().setPortMode(port, mode);
    }

}
