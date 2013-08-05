package lejos.nxt;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import lejos.internal.io.NativeDevice;
import lejos.util.Delay;

public class UARTPort implements EV3SensorConstants
{
    protected static NativeDevice uart;
    protected static Pointer pDev;
    protected static ByteBuffer devStatus;
    protected static ByteBuffer raw;
    protected static ByteBuffer actual;
    protected static byte [] dc = new byte[3*PORTS];
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
    
    protected static final int TIMEOUT_DELTA = 10;
    protected static final int TIMEOUT = 4000;
    protected static final int INIT_DELAY = 5;
    protected static final int INIT_RETRY = 100;
    protected static final int OPEN_RETRY = 5;
    
    static {
        initDeviceIO();
    }

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
    
    protected int port;
    protected TYPES[] modeInfo = new TYPES[UART_MAX_MODES];
    protected int mode = 0;
    /**
     * Create and return a devCon structure ready for use.
     * @param p
     * @param conn
     * @param typ
     * @param mode
     * @return
     */
    private static byte[] devCon(int p, int conn, int typ, int mode)
    {
        // structure is 3 byte arrays
        //byte [] dc = new byte[3*PORTS];
        dc[p] = (byte)conn;
        dc[p + PORTS] = (byte) typ;
        dc[p + 2*PORTS] = (byte) mode;
        return dc;
    }

    protected byte getStatus()
    {
        return devStatus.get(port);
    }
    
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
        return status;       
    }
    
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
        return status;       
    }

    protected void reset()
    {
        // Force the device to reset
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_NONE, 0, 0));        
    }

    protected void setOperatingMode(int mode)
    {
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_INPUT_UART, 0, mode));        
    }

    protected boolean getModeInfo(int mode, UARTCTL uc)
    {
        uc.Port = (byte)port;
        uc.Mode = (byte)mode;
        uc.write();
        System.out.println("size is " + uc.size() + " TYPES " + uc.TypeData.size() + " ptr " + uc.getPointer().SIZE);
        uart.ioctl(UART_READ_MODE_INFO, uc.getPointer());
        uc.read();
        System.out.println("name[0]" + uc.TypeData.Name[0]);
        return uc.TypeData.Name[0] != 0;
    }

    protected void clearPortChanged()
    {
        //System.out.printf("Clear changed\n");
        uart.ioctl(UART_CLEAR_CHANGED, devCon(port, CONN_INPUT_UART, 0, 0));
        devStatus.put(port, (byte)(devStatus.get(port) & ~UART_PORT_CHANGED));        
    }
    
    protected boolean initSensor(int port, int mode)
    {
        this.port = port;
        byte status;
        int retryCnt = INIT_RETRY;
        long base = System.currentTimeMillis();
        // Force the device to reset
        reset();
        status = waitZeroStatus(TIMEOUT);
        if (status != 0)
            return false;
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        //if (!getModeInfo(0, uc))
            //System.out.println("Got info fails");
            
        // initialise the device
        //System.out.println("status is " + getStatus());
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        // now try and configure as a UART
        setOperatingMode(0);
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        status = waitNonZeroStatus(TIMEOUT);
        //System.out.println("status is " + getStatus());
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
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
                //System.out.println("Time is " + (System.currentTimeMillis() - base));
                //System.out.println("status is " + devStatus.get(port));
                //Delay.msDelay(INIT_DELAY);
                //System.out.println("status is " + devStatus.get(port));
                //setOperatingMode(mode);
                Delay.msDelay(5);
                //System.out.println("Time is " + (System.currentTimeMillis() - base));
                //System.out.println("status is " + devStatus.get(port));
                status = waitNonZeroStatus(TIMEOUT);
                //System.out.println("Time is " + (System.currentTimeMillis() - base));
                if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0) 
                {
                    System.out.println("Sensor is ready retry cnt " + retryCnt);
                    for(int i = 0; i < UART_MAX_MODES; i++)
                    {
                        UARTCTL uc = new UARTCTL();
                        if (getModeInfo(i, uc))
                        {
                            modeInfo[i] = uc.TypeData;
                        }
                        else
                            modeInfo[i] = null;
                    }
                    status = waitNonZeroStatus(TIMEOUT);
                    if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0) 
                        return setMode(mode);
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
    
    
    public boolean open(int port, int mode)
    {
        for(int i = 0; i < OPEN_RETRY; i++)
            if (initSensor(port, mode))
            {
                System.out.println("reset cnt " + i);
                return true;
            }
        return false;
    }

    public boolean setMode(int mode)
    {
        setOperatingMode(mode);
        int status = waitNonZeroStatus(TIMEOUT);
        if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
        {
            this.mode = mode;
            return true;
        }
        else
            return false;
    }
    
    private int calcRawOffset()
    {
        return port*DEV_RAW_SIZE1 + actual.getShort(port*2)*DEV_RAW_SIZE2;
    }

    public byte getByte()
    {
        return raw.get(calcRawOffset());
    }
    
    public void getBytes(byte [] vals, int offset, int len)
    {
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.get(loc + i);
    }

    public int getShort()
    {
        return raw.getShort(calcRawOffset());
    }
    
    public void getShorts(short [] vals, int offset, int len)
    {
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.get(loc + i*2);
    }

    public String getModeName(int mode)
    {
        if (modeInfo[mode] != null)
            return new String(modeInfo[mode].Name);
        else 
            return "Unknown";
    }

    public String toString()
    {
        float divTable[] = {1f, 10f, 100f, 1000f, 10000f, 100000f};
        TYPES info = modeInfo[mode];
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
