package lejos.robotics;

public class DirectionFinderSensor implements DirectionFinder {
	private CalibratedSampleProvider provider;
	private float[] sample = new float[1];
	
	public DirectionFinderSensor(CalibratedSampleProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public float getDegreesCartesian() {
		provider.fetchSample(sample, 0);
		return sample[0];
	}

	@Override
	public void startCalibration() {
		provider.startCalibration();
	}

	@Override
	public void stopCalibration() {	
		provider.stopCalibration();
	}

	@Override
	public void resetCartesianZero() {
		provider.resetCartesianZero();
	}

}
