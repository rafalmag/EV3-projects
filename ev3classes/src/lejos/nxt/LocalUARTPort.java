package lejos.nxt;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import lejos.internal.io.NativeDevice;
import lejos.util.Delay;

/**
 * Provide access to EV3 sensor ports operating in UART mode.<p><p>
 * NOTE: This code is not pretty! The interface uses a number of structures mapped
 * into memory from the device. I am not aware of any clean way to implement this
 * interface in Java. So for now multiple pointers to bytes/ints array etc. are used this
 * means that the actual offsets of the start of the C arrays needs to be obtained
 * and these (along with various sizes) are currently hard-coded as "OFF" values below.
 * I'm sure there must be a better way! Also note that there seem to be a large
 * number of potential race conditions in the device initialisation stage hence the
 * various loops needed to retry operations.
 * @author andy
 *
 */
public class LocalUARTPort extends LocalSensorPort implements UARTPort
{
    protected static NativeDevice uart;
    protected static Pointer pDev;
    protected static ByteBuffer devStatus;
    protected static ByteBuffer raw;
    protected static ByteBuffer actual;
    protected static final int DEV_SIZE = 42744;
    protected static final int DEV_STATUS_OFF = 42608;
    protected static final int DEV_RAW_OFF = 4192;
    protected static final int DEV_RAW_SIZE1 = 9600;
    protected static final int DEV_RAW_SIZE2 = 32;
    protected static final int DEV_ACTUAL_OFF = 42592;
    
    protected static final int UART_SET_CONN = 0xc00c7500;
    protected static final int UART_READ_MODE_INFO = 0xc03c7501;
    protected static final int UART_NACK_MODE_INFO = 0xc03c7502;
    protected static final int UART_CLEAR_CHANGED = 0xc03c7503;
    
    protected static final byte UART_PORT_CHANGED = 1;
    protected static final byte UART_DATA_READY = 8;
    
    protected static final int TIMEOUT_DELTA = 5;
    protected static final int TIMEOUT = 4000;
    protected static final int INIT_DELAY = 5;
    protected static final int INIT_RETRY = 100;
    protected static final int OPEN_RETRY = 5;
    
    static {
        initDeviceIO();
    }

    /**
     * The following class maps directly to a C structure containg device information.
     * @author andy
     *
     */
    public static class TYPES extends Structure
    {
        public byte Name[] = new byte[12];
        public byte Type;
        public byte Connection;
        public byte Mode;
        public byte DataSets;
        public byte Format;
        public byte Figures;
        public byte Decimals;
        public byte Views;
        public float RawMin;
        public float RawMax;
        public float PctMin;
        public float PctMax;
        public float SiMin;
        public float SiMax;
        public short InvalidTime;
        public short IdValue;
        public byte  Pins;
        public byte[] Symbol = new byte[5];
        public short     Align;
        
        public TYPES()
        {
            this.setAlignType(Structure.ALIGN_NONE);
        }
    }
    
    public static class UARTCTL extends Structure
    {
        public TYPES TypeData;
        public byte Port;
        public byte Mode;
        
        public UARTCTL()
        {
            this.setAlignType(Structure.ALIGN_NONE);
        }

    }
    
    protected TYPES[] modeInfo = new TYPES[UART_MAX_MODES];

    /**
     * return the current status of the port
     * @return status
     */
    protected byte getStatus()
    {
        return devStatus.get(port);
    }

    /**
     * Wait for the port status to become non zero, or for the operation to timeout
     * @param timeout timeout period in ms
     * @return port status or 0 if the operation timed out
     */
    protected byte waitNonZeroStatus(int timeout)
    {
        int cnt = timeout/TIMEOUT_DELTA;
        byte status = getStatus();
        while (cnt-- > 0)
        {
            if (status != 0)
                return status;
            Delay.msDelay(TIMEOUT_DELTA);
            status = getStatus();
        }
        System.out.println("NZS Timeout");
        return status;       
    }

    /**
     * Wait for the port status to become zero
     * @param timeout timeout period in ms
     * @return zero if successful or the current status if timed out
     */
    protected byte waitZeroStatus(int timeout)
    {
        int cnt = timeout/TIMEOUT_DELTA;
        byte status = getStatus();
        while (cnt-- > 0)
        {
            if (status == 0)
                return status;
            Delay.msDelay(TIMEOUT_DELTA);
            status = getStatus();
        }
        System.out.println("ZS Timeout");
        return status;       
    }

    /**
     * reset the port and device
     */
    protected void reset()
    {
        // Force the device to reset
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_NONE, 0, 0));        
    }

    /**
     * Set the current operating mode
     * @param mode
     */
    protected void setOperatingMode(int mode)
    {
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_INPUT_UART, 0, mode));        
    }

    /**
     * Read the mode information for the specified operating mode.
     * @param mode mode number to read
     * @param uc control structure to read the data into
     * @return
     */
    protected boolean getModeInfo(int mode, UARTCTL uc)
    {
        uc.Port = (byte)port;
        uc.Mode = (byte)mode;
        uc.write();
        //System.out.println("size is " + uc.size() + " TYPES " + uc.TypeData.size() + " ptr " + uc.getPointer().SIZE);
        uart.ioctl(UART_READ_MODE_INFO, uc.getPointer());
        uc.read();
        //System.out.println("name[0]" + uc.TypeData.Name[0]);
        return uc.TypeData.Name[0] != 0;
    }

    /**
     * Clear the port changed flag for the current port.
     */
    protected void clearPortChanged()
    {
        //System.out.printf("Clear changed\n");
        uart.ioctl(UART_CLEAR_CHANGED, devCon(port, CONN_INPUT_UART, 0, 0));
        devStatus.put(port, (byte)(devStatus.get(port) & ~UART_PORT_CHANGED));        
    }

    /**
     * Attempt to initialise the sensor ready for use.
     * @param mode initial operating mode
     * @return true if the initialisation succeeded false if it failed
     */
    protected boolean initSensor(int mode)
    {
        byte status;
        int retryCnt = INIT_RETRY;
        long base = System.currentTimeMillis();
       // Force the device to reset
        reset();
        status = waitZeroStatus(TIMEOUT);
        if (status != 0)
            return false;
        System.out.println("Time is " + (System.currentTimeMillis() - base));
        //if (!getModeInfo(0, uc))
            //System.out.println("Got info fails");
            
        // initialise the device
        //System.out.println("status is " + getStatus());
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        // now try and configure as a UART
        setOperatingMode(mode);
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        status = waitNonZeroStatus(TIMEOUT);
        //System.out.println("status is " + getStatus());
        System.out.println("Time is " + (System.currentTimeMillis() - base));
        while((status & UART_PORT_CHANGED) != 0 && retryCnt-- > 0)
        {
            //System.out.println("About to get mode data retry " + retryCnt);

            // When mode zero data is available the device is almost ready!
            //if (getModeInfo(0, uc))
            {
                //System.out.println("Time is " + (System.currentTimeMillis() - base));
                //Delay.msDelay(5);
                //clearModeInfoCache(0);
                clearPortChanged();
                System.out.println("L1 Time is " + (System.currentTimeMillis() - base));
                //System.out.println("status is " + devStatus.get(port));
                //Delay.msDelay(INIT_DELAY);
                //System.out.println("status is " + devStatus.get(port));
                //setOperatingMode(mode);
                Delay.msDelay(INIT_DELAY);
                //System.out.println("Time is " + (System.currentTimeMillis() - base));
                //System.out.println("status is " + devStatus.get(port));
                status = waitNonZeroStatus(TIMEOUT);
                System.out.println("L2 Time is " + (System.currentTimeMillis() - base));
                if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0) 
                {
                    int modeCnt = 0;
                    System.out.println("Sensor is ready retry cnt " + retryCnt);
                    for(int i = 0; i < UART_MAX_MODES; i++)
                    {
                        UARTCTL uc = new UARTCTL();
                        if (getModeInfo(i, uc))
                        {
                            modeInfo[i] = uc.TypeData;
                            modeCnt++;
                        }
                        else
                            modeInfo[i] = null;
                    }
                    System.out.println("L3 Time is " + (System.currentTimeMillis() - base));
                    System.out.println("Mode cnt " + modeCnt);
                    status = waitNonZeroStatus(TIMEOUT);
                    if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
                    {
                        setOperatingMode(mode);
                        status = waitNonZeroStatus(TIMEOUT);
                        if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
                            return super.setMode(mode);
                    }
                    System.out.println("Failed during get info");
                }
                //return false;
            }
            Delay.msDelay(INIT_DELAY);
            //System.out.println("status is " + devStatus.get(port));
            status = waitNonZeroStatus(TIMEOUT);
       }
       return false;
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean initialiseSensor(int mode)
    {
        for(int i = 0; i < OPEN_RETRY; i++)
            if (initSensor(mode))
            {
                System.out.println("reset cnt " + i);
                return true;
            }
        return false;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public void resetSensor()
    {
        reset();
    }

    /**
     * Open the port and set the initial operating mode.
     * @param port port number to open
     * @param mode operating mode
     * @return true if post was opened, false if the operation failed
     */
    public boolean open(int port, int mode)
    {
        if (!super.open(port))
            return false;
        if (initialiseSensor(mode))
            return true;
        super.close();
        return false;
    }

    
    /** {@inheritDoc}
     */    
    @Override
    public boolean open(int port)
    {
        return open(port, 0);
    }

    /** {@inheritDoc}
     */    
    @Override
    public void close()
    {
        reset();
        super.close();
    }
    
    /**
     * Set the current operating mode
     * @param mode new mode to set
     * @return true if the mode is set, false if the operation failed
     */
    public boolean setMode(int mode)
    {
        System.out.println("Set mode " + mode);
        if (modeInfo[mode] == null)
            return false;
        System.out.println("Mode is " + getModeName(mode));
        setOperatingMode(mode);
        int status = waitNonZeroStatus(TIMEOUT);
        System.out.println("status is " + status);
        if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
        {
            return super.setMode(mode);
        }
        else
            // Sensor may have reset try and initialise it in the new mode.
            return initialiseSensor(mode);
    }

    /**
     * The RAW data is held in a circular buffer with 32 bytes of data per entry
     * and 300 entries per port. This method calculates the byte offset of the
     * latest data value read into the buffer.
     * @return offset of the current data
     */
    private int calcRawOffset()
    {
        //System.out.println("actual " + actual.getShort(port*2));
        return port*DEV_RAW_SIZE1 + actual.getShort(port*2)*DEV_RAW_SIZE2;
    }

    /**
     * read a single byte from the device
     * @return the byte value
     */
    public byte getByte()
    {
        return raw.get(calcRawOffset());
    }

    /**
     * read a number of bytes from the device
     * @param vals byte array to accept the data
     * @param offset offset at which to store the data
     * @param len number of bytes to read
     */
    public void getBytes(byte [] vals, int offset, int len)
    {
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.get(loc + i);
    }

    /**
     * read a single short from the device.
     * @return the short value
     */
    public int getShort()
    {
        return raw.getShort(calcRawOffset());
    }
    
    /**
     * read a number of shorts from the device
     * @param vals short array to accept the data
     * @param offset offset at which to store the data
     * @param len number of shorts to read
     */
    public void getShorts(short [] vals, int offset, int len)
    {
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.get(loc + i*2);
    }

    /**
     * Get the string name of the specified mode.<p><p>
     * TODO: Make other mode data available.
     * @param mode mode to lookup
     * @return String version of the mode name
     */
    public String getModeName(int mode)
    {
        if (modeInfo[mode] != null)
            return new String(modeInfo[mode].Name);
        else 
            return "Unknown";
    }

    /**
     * Return the current sensor reading to a string. 
     */
    public String toString()
    {
        float divTable[] = {1f, 10f, 100f, 1000f, 10000f, 100000f};
        TYPES info = modeInfo[currentMode];
        float val;
        switch(info.Format)
        {
        case 0:
            if (info.RawMin >= 0)
                val = getByte() & 0xff;
            else
                val = getByte();
            break;
        case 1:
            if (info.RawMin >= 0)
                val = getShort() & 0xffff;
            else
                val = getShort();
            break;
        // TODO: Sort out other formats
        default:
            val = 0.0f;
        }
        val = val/divTable[info.Decimals];
        String format = "%" + info.Figures + "." + info.Decimals + "f" + new String(info.Symbol);
        return String.format(format, val);        
    }

    /**
     * Reset all of the ports
     */
    public static void resetAll()
    {
        // reset everything
        for(int i = 0; i < PORTS; i++)
            devCon(i, CONN_NONE, 0, 0);
        uart.ioctl(UART_SET_CONN, dc);        
    }
    
    private static void initDeviceIO()
    {
        uart = new NativeDevice("/dev/lms_uart");
        pDev = uart.mmap(DEV_SIZE);
        devStatus = pDev.getByteBuffer(DEV_STATUS_OFF, PORTS);
        actual = pDev.getByteBuffer(DEV_ACTUAL_OFF, PORTS*2);
        raw = pDev.getByteBuffer(DEV_RAW_OFF, PORTS*DEV_RAW_SIZE1);
    }
}
