package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/** Baase class for Sample filters
 * @author Kirk, Aswin
 *
 */
public abstract class AbstractFilter implements SampleProvider {
	protected final SampleProvider source;
	protected final int sampleSize;
	
	public AbstractFilter(SampleProvider source) {
		this.source=source;
		this.sampleSize=source.sampleSize();
	}

	@Override
	public int sampleSize() {
		return sampleSize;
	}

	@Override
	public int fetchSample(float[] sample, int offset) {
		return source.fetchSample(sample, offset);
	}

}
