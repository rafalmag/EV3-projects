package lejos.robotics;

public interface CalibratedSampleProvider extends SampleProvider {

	/**
	 * Starts calibration.
	 * Must call stopCalibration() when done.
	 */
	public void startCalibration();
	
	/**
	 * Ends calibration sequence.
	 */
	public void stopCalibration();
	
	/**
	 * Changes the current direction the compass is facing into the zero 
	 * angle. 
	 */
	public void resetCartesianZero();
	
}
