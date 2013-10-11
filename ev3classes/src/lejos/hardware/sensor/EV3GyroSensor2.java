package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.SampleProvider;

/**
 * Sensor driver for the Lego EV3 Gyro sensor.<br>
 * 
 * @author Aswin Bouwmeester
 */
public class EV3GyroSensor2 extends UARTSensor {

	private static final long	SWITCHDELAY	= 200;
	private static final int	CALIBRATEMODE	= 4;

	/**
	 * Represent the gyro sensor in rate mode. <br>
	 * In rate mode the sensor measures the speed of rotation expressed in degrees/second. 
	 * A positive rate indicates a counterclockwise rotation. A negative rate indicates a clockwise rotation.
	 */
	public final SampleProvider	rateMode;
	/**
	 * Represent the gyro sensor in angle mode. <br>
	 * In rate mode the sensor measures the orientation of the sensor in repect to its start position. 
	 * A positive angle indicates a orientation to the left. A negative rate indicates a rotation to the right.
	 * Angles are expressed in degrees and fall in the range of -180 to 180.
	 */
	public final SampleProvider	angleMode;

	public EV3GyroSensor2(Port port) {
		super(port);
		rateMode = new RateMode();
		angleMode = new AngleMode();
	}

	public EV3GyroSensor2(UARTPort port) {
		super(port);
		rateMode = new RateMode();
		angleMode = new AngleMode();
	}
	
	/** Hardware calibration of the Gyro sensor. <br>
	 * The sensor should be motionless during calibration.
	 */
	public void Calibrate() {
		// TODO: Test if angle is reset to zero due to calibration
		// TODO: find out how to get out of calibration mode
		switchMode(CALIBRATEMODE, SWITCHDELAY);
	}


	private class AngleMode implements SampleProvider {
		private static final int	MODE	= 0;
		// TODO: Decide on unit for angles.
		// TODO: Decide on right hand rule.
		private static final float toSI=1;


		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			switchMode(MODE, SWITCHDELAY);
			sample[offset] = port.getByte(); // * toSI;
		}

	}

	private class RateMode implements SampleProvider {
		private static final int	MODE	= 1;
		// TODO: Decide on unit for angles.
		// TODO: Decide on right hand rule.
		private static final float toSI=1;


		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			switchMode(MODE, SWITCHDELAY);
			sample[offset] = port.getByte(); // * toSI;
		}

	}


}
