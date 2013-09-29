package lejos.internal.ev3;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import lejos.internal.io.NativeDevice;
import lejos.nxt.I2CPort;
import lejos.util.Delay;

/**
    Provide access to EV3 I2C sensors.<BR>
    NOTE: The EV3 iic kernel module provides the capability to make an i2c sensor 
    have a similar interface to that used for uart based sensors. In particular it
    provides a mechanism to have the kernel poll the sensor. However this mode seems
    to be of limited use because most i2c sensors provide multiple data values etc.
    Because of this we only implement the basic i2c interface.
 */
public class EV3I2CPort extends EV3IOPort implements I2CPort
{
    protected static NativeDevice i2c;
    protected static Pointer pIic;
    protected static ByteBuffer iicStatus;
    protected static ByteBuffer iicChanged;
    static {
        initDeviceIO();
    }
    protected static final int IIC_SET_CONN = 0xc00c6902;
    protected static final int IIC_READ_TYPE_INFO = 0xc03c6903;
    protected static final int IIC_SETUP = 0xc04c6905;
    protected static final int IIC_SET = 0xc02c6906;
    
    protected static final int IIC_SIZE = 42748;
    protected static final int IIC_STATUS_OFF = 42608;
    protected static final int IIC_CHANGED_OFF = 42612;
    
    protected static final int IO_TIMEOUT = 2000;

    public static class IICDATA extends Structure
    {
      public int  Result;
      public byte Port;
      public byte Repeat;
      public short Time;
      public byte WrLng;
      public byte[] WrData = new byte[IIC_DATA_LENGTH];
      public byte RdLng;
      public byte[] RdData = new byte[IIC_DATA_LENGTH];
      
      public IICDATA()
      {
          this.setAlignType(Structure.ALIGN_NONE);
      }
    };
    protected IICDATA iicdata = new IICDATA();
    
    public static final int STANDARD_MODE = 0;
    public static final int LEGO_MODE = 1;
    public static final int ALWAYS_ACTIVE = 2;

   
    /** Do not release the i2c bus between requests */
    public static final int NO_RELEASE = 4;
    /** Use high speed I/O (125KHz) */
    public static final int HIGH_SPEED = 8;
    /** Maximum read/write request length */
    public static final int MAX_IO = IIC_DATA_LENGTH;

    protected boolean getChanged()
    {
        return iicChanged.get(port) != 0;
    }
    
    protected byte getStatus()
    {
        return iicStatus.get(port);
    }

    protected void reset()
    {
        i2c.ioctl(IIC_SET_CONN, devCon(port, CONN_NONE, 0, 0));        
    }

    protected void setOperatingMode(int typ, int mode)
    {
        i2c.ioctl(IIC_SET_CONN, devCon(port, CONN_NXT_IIC, typ, mode));        
    }
    
    protected boolean initSensor()
    {
        // Set pin configuration and power for standard i2c sensor.
        setPinMode(CMD_FLOAT);
        reset();
        Delay.msDelay(100);
        setOperatingMode(TYPE_IIC_UNKNOWN, 255);
        Delay.msDelay(100);
        setOperatingMode(TYPE_IIC_UNKNOWN, 255);        
        Delay.msDelay(100);
        //System.out.println("Status " + getStatus() + " changed " + getChanged());
        return true;
    }
    
    /**
     * allow access to the specified port
     * @param p port number to open
     */
    public boolean open(int t, int p, EV3Port r)
    {
        if (!super.open(t, p, r))
            return false;
        if (!initSensor())
        {
            super.close();
            return false;
        }
        return true;
    }
    
    /**
     * High level i2c interface. Perform a complete i2c transaction and return
     * the results. Writes the specified data to the device and then reads the
     * requested bytes from it.
     * @param deviceAddress The I2C device address.
     * @param writeBuf The buffer containing data to be written to the device.
     * @param writeOffset The offset of the data within the write buffer
     * @param writeLen The number of bytes to write.
     * @param readBuf The buffer to use for the transaction results
     * @param readOffset Location to write the results to
     * @param readLen The length of the read
     * @return < 0 error otherwise the number of bytes read
     */
    public synchronized int i2cTransaction(int deviceAddress, byte[]writeBuf,
            int writeOffset, int writeLen, byte[] readBuf, int readOffset,
            int readLen)
    {
        long timeout = System.currentTimeMillis() + IO_TIMEOUT;
        //System.out.println("ioctl: " + deviceAddress + " wlen " + writeLen);
        iicdata.Port = (byte)port;
        iicdata.Result = -1;
        iicdata.Repeat = 1;
        iicdata.Time = 0;
        iicdata.WrLng = (byte)(writeLen + 1);
        System.arraycopy(writeBuf, writeOffset, iicdata.WrData, 1, writeLen);
        iicdata.WrData[0] = (byte)(deviceAddress >> 1);
        iicdata.WrLng = (byte)(writeLen + 1);
        // note -ve value due to Lego's crazy reverse order stuff
        iicdata.RdLng = (byte)-readLen;
        iicdata.write();
        while(timeout > System.currentTimeMillis())
        {
            iicdata.write();
            i2c.ioctl(IIC_SETUP, iicdata.getPointer());
            iicdata.read();
            //System.out.println("Ioctl result: " + iicdata.Result);
            if (iicdata.Result < 0)
                return -1;
            if (iicdata.Result == STATUS_OK)
            {
                if (readLen > 0)
                    System.arraycopy(iicdata.RdData, 0, readBuf, readOffset,  readLen);
                return readLen;
            }
            Thread.yield();
        }
        //System.out.println("Timeout");
        return -1;
    }
    
    
    private static void initDeviceIO()
    {
        i2c = new NativeDevice("/dev/lms_iic");
        pIic = i2c.mmap(IIC_SIZE);
        iicStatus = pIic.getByteBuffer(IIC_STATUS_OFF, PORTS);
        iicChanged = pIic.getByteBuffer(IIC_CHANGED_OFF, PORTS*2);
    }
    
}
