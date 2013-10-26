package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.EndianTools;

/**
 * Supports Mindsensors DIST-Nx series of Optical Distance Sensor.<br>
 * This sensor is used for greater precision than the Ultrasonic Sensor.<br>
 * 
 * See http://www.mindsensors.com/index.php?module=pagemaster&PAGE_user_op=view_page&PAGE_id=73
 * 
 * @author Michael Smith <mdsmitty@gmail.com>
 * <br><br>Lum, Many thanks for helping me test this class.
 * 
 */
public class MindsensorsDistanceSensor extends I2CSensor implements SensorMode {
	private byte[] buf = new byte[2];
	
	//Registers
	private final static int COMMAND = 0x41;
	private final static int DIST_DATA_LSB = 0x42;

	//Commands
	private final static byte DE_ENERGIZED = 0x44;
	private final static byte ENERGIZED = 0x45;
	
	/**
	 *
	 * @param port NXT sensor port 1-4
	 */
	public MindsensorsDistanceSensor(I2CPort port){
	    this(port, DEFAULT_I2C_ADDRESS);
	    init();
	}

    /**
     *
     * @param port NXT sensor port 1-4
     * @param address I2C address for the sensor
     */
    public MindsensorsDistanceSensor(I2CPort port, int address){
        super(port, address);
        init();
    }
     
    /**
     *
     * @param port NXT sensor port 1-4
     */
    public MindsensorsDistanceSensor(Port port){
        this(port, DEFAULT_I2C_ADDRESS);
        init();
    }

    /**
     *
     * @param port NXT sensor port 1-4
     * @param address I2C address for the sensor
     */
    public MindsensorsDistanceSensor(Port port, int address){
        super(port, address, TYPE_LOWSPEED);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    	powerOn();
    }

	/**
	 * Turns the sensor module on.  <br>
	 * Power is turned on by the constructor method.
	 *
	 */
	public void powerOn(){
		sendData(COMMAND, ENERGIZED);
	}
	
	/**
	 * Turns power to the sensor module off.
	 *
	 */
	public void powerOff(){
		sendData(COMMAND, DE_ENERGIZED);
	}
	
	/**
	 * Returns a sample provider in distance mode.
	 */
	public SensorMode getDistanceMode() {
		return this;
	}
	
	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(DIST_DATA_LSB, buf, 2);
		sample[offset] = (float) EndianTools.decodeIntLE(buf, 0) / 100f;		
	}

	@Override
	public String getName() {
		return "Distance";
	}
}
