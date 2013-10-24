package lejos.hardware.sensor;


import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.utility.EndianTools;

/**
 * Represents the Dexter Industries dIMU sensor.<br>
 * The IMU sensor provider acceleration (m/s), rate (degrees/sec) and
 * temperature (Celcius) data. <br>
 * <a href="http://dexterindustries.com/files/dIMU_Datasheets.zip">IMU sensor
 * datasheets</a>
 * 
 * @author Aswin
 * 
 */
public class DexterIMUSensor {
  // TODO: Add support for sensor configuration
  // TODO: use both I2CPort and Port types

  // I2C Addresses for the gyro and acceleration chips with the default values
  protected int               Accel_I2C_address = 0x3A;
  protected int               Gyro_I2C_address  = 0xD2;
  private Port                port;
  private I2CPort             I2Cport;

  private SampleProvider      accel;
  private DexterIMUGyroSensor gyro;

  public DexterIMUSensor(I2CPort port) {
    this.I2Cport = port;
    System.out.println("I2CPort");
  }
  

  public DexterIMUSensor(Port port) {
    this.port = port;
    System.out.println("Port");
  }

  /**
   * Gives a SampleProvider thst returns acceleration (m/s) samples.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getAccelerationMode() {
    if (accel == null) {
      accel = new DexterIMUAccelerationSensor(port, Accel_I2C_address);
    }
    return accel;
  }

  /**
   * Gives a SampleProvider thst returns rate (degree/s) samples.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getGyroMode() {
    if (gyro == null) {
      gyro = new DexterIMUGyroSensor(port, Gyro_I2C_address);
    }
    return gyro.getGyroMode();
  }

  /**
   * Gives a SampleProvider thst returns temperature (degree Celcius) samples.<br>
   * Note that this is the temperature of the gyro chip. This does not have to
   * correspond to the temperture of the surrounding.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getTemperatureMode() {
    if (gyro == null) {
      gyro = new DexterIMUGyroSensor(port, Gyro_I2C_address);
    }
    return gyro.getTemperatureMode();
  }

  /**
   * Provides access to the gyro sensor on the IMU. <br>
   * The Gyro sensor is the L3G4200D from ST.
   * 
   * @author Aswin
   * 
   */
  private class DexterIMUGyroSensor extends I2CSensor {

    // Register adresses
    private static final int CTRL_REG1   = 0x020;
    private static final int CTRL_REG2   = 0x021;
    private static final int CTRL_REG3   = 0x022;
    private static final int CTRL_REG4   = 0x023;
    private static final int CTRL_REG5   = 0x024;
    private static final int REG_STATUS  = 0x27;

    // Configurable parameters
    // This sensor can configured for samplerate and range. Currently only the
    // default values are set.
    // The code to set other values is there.

    // private float[] RATES = { 100,200,400,800};
    private int[]            RATECODES   = { 0x00, 0x40, 0x80, 0xC0 };
    // private float[] RANGES = { 250,500,2000};
    private int[]            RANGECODES  = { 0x00, 0x10, 0x20 };
    private float[]          MULTIPLIERS = { 8.75f, 17.5f, 70f };

    private SampleProvider   temperatureMode;
    private SampleProvider   gyroMode;
    private int              range       = 2;
    private int              rate        = 3;
    private float            toSI        = MULTIPLIERS[range] / 1000f;

    private byte[]           buf         = new byte[7];

    public DexterIMUGyroSensor(Port port, int address) {
      super(port, address, TYPE_LOWSPEED_9V);
      init();
    }

    /**
     * This method configures the sensor
     */
    private void init() {
      int reg;
      // put in sleep mode;
      sendData(CTRL_REG1, (byte) 0x08);
      // oHigh-pass cut off 1 Hz;
      sendData(CTRL_REG2, (byte) 0x01);
      // no interrupts, no fifo
      sendData(CTRL_REG3, (byte) 0x08);
      // set range
      reg = RANGECODES[range] | 0x80;
      toSI = MULTIPLIERS[range] / 1000f;
      sendData(CTRL_REG4, (byte) reg);
      // disable fifo and high pass
      sendData(CTRL_REG5, (byte) 0x00);
      // stabilize output signal;
      // enable all axis, set output data rate ;
      reg = RATECODES[rate] | 0xF;
      // set sample rate, wake up
      sendData(CTRL_REG1, (byte) reg);
      float[] dummy = new float[3];
      SampleProvider gyro = getGyroMode();
      for (int s = 1; s <= 15; s++) {
        while (!isNewDataAvailable())
          Thread.yield();
        gyro.fetchSample(dummy, 0);
      }
    }

    /**
     * Returns true if new data is available from the sensor
     */
    private boolean isNewDataAvailable() {
      getData(REG_STATUS, buf, 1);
      if ((buf[0] & 0x08) == 0x08)
        return true;
      return false;
    }

    /**
     * Provides access to the temperature data on the yro sensor
     * 
     * @return
     */
    public SampleProvider getTemperatureMode() {
      if (temperatureMode == null) {
        temperatureMode = new TemperatureMode();
      }
      return temperatureMode;
    }

    /**
     * Provides access to the rate data on the gyro sensor
     * 
     * @return
     */
    public SampleProvider getGyroMode() {
      if (temperatureMode == null) {
        gyroMode = new GyroMode();
      }
      return gyroMode;
    }

    /**
     * Represent the gyro sensor in temperature mode
     * 
     * @author Aswin
     * 
     */
    private class TemperatureMode implements SampleProvider {
      private static final int DATA_REG = 0x26 | 0x80;

      @Override
      public int sampleSize() {
        return 1;
      }

      @Override
      public void fetchSample(float[] sample, int offset) {
        // TODO Test method, maybe temperature needs to be converted
        getData(DATA_REG, buf, 7);
        sample[offset] = buf[0];
      }

    }

    /**
     * Represent the gyro sensor in rate mode
     * 
     * @author Aswin
     * 
     */
    private class GyroMode implements SampleProvider {
      private static final int DATA_REG = 0x27 | 0x80;

      @Override
      public int sampleSize() {
        return 3;
      }

      @Override
      public void fetchSample(float[] sample, int offset) {
        buf[0] = 0;
        int attempts = 0;
        // loop while no new data available or data overrun occured, break out
        // after 20 attempts
        while (((buf[0] & 0x80) == 0x80 || (buf[0] & 0x08) != 0x08) && attempts++ <= 20) {
          getData(DATA_REG, buf, 7);
          if ((buf[0] & 0x08) != 0x08)
            Thread.yield();
        }
        if (attempts == 20) {
          // No succesfull read, return NaN
          for (int i = 0; i < 3; i++) {
            sample[i + offset] = Float.NaN;
          }
        }
        else {
          for (int i = 0; i < 3; i++) {
            sample[i + offset] = (float) ((((buf[i + 2]) << 8) | (buf[i + 1] & 0xFF)) * toSI);
          }
        }
      }
    }
  }

  /**
   * Class to access the Acceleration sensor from Dexter Industries IMU
   * the acceleration sensor is the Freescale MMA7455
   * 
   * @author Aswin
   * 
   */
  public class DexterIMUAccelerationSensor extends I2CSensor implements SampleProvider {
    protected static final int   DATA_REG = 0x00;
    protected static final int   MODE_REG = 0x16;
    protected static final float TOSI     = 1.0f / (64.0f * 9.81f);

    private byte[]               buf      = new byte[6];

    private DexterIMUAccelerationSensor(Port port, int address) {
      super(port, address, I2CPort.TYPE_LOWSPEED_9V);
      //System.out.println("Port, Address: "+port.getName()+", " + this.address);
      sendData(MODE_REG, (byte) 0x05);
    }

    @Override
    public int sampleSize() {
      return 3;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      getData(DATA_REG, buf, 6);
      System.out.print("GetData buf: ");
      for (int i = 0; i < 6; i++)
        System.out.print(" " + buf[i]);
      System.out.println();
      for (int i = 0; i < 3; i++) {
        sample[i + offset] = EndianTools.decodeShortBE(buf, i);
        if ((buf[i + 1] & 0x02) != 0)
          sample[i + offset] = -(sample[i + offset] - 512);
        sample[i + offset] *= TOSI;
      }

    }
  }
}
