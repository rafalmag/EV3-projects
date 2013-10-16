package lejos.robotics.filter;

import lejos.robotics.SampleProvider;



/**
 * Provides a low-pass filter for samples <br>
 * @see <a href=http://en.wikipedia.org/wiki/Low-pass_filter>http://en.wikipedia.org/wiki/Low-pass_filter</a>
 * @author Aswin
 *
 */
public class LowPassFilter extends AbstractFilter{
	float[] smoothed;
	long lastTime;
	float timeConstant;

	/**
	 * Constructor
	 * @param source
	 * The source for getting samples
	 * @param timeConstant
	 * The cut-off frequency for the filter
	 */
	public LowPassFilter(SampleProvider source, float timeConstant) {
		super(source);
		smoothed=new float[sampleSize];
		lastTime=System.currentTimeMillis();
		this.timeConstant=timeConstant;
	}

	/**
	 * Fetches a sample from the source and low-passes it
	 * @see http://en.wikipedia.org/wiki/Low-pass_filter
	 */
	public int fetchSample(float[] dst, int off) {
		int rc=super.fetchSample(dst,off);
		if (rc==0){ 
		if (lastTime==0) {
			for (int axis=0;axis<sampleSize;axis++) {
				smoothed[axis]=(dst[off+axis]);
				lastTime=System.currentTimeMillis();
			}
		}
		else {
			float dt=(float) ((System.currentTimeMillis()-lastTime)/1000.0);
			lastTime=System.currentTimeMillis();
			float a=dt/(timeConstant+dt);
			for (int axis=0;axis<sampleSize;axis++) {
				smoothed[axis]=smoothed[axis]+a*(dst[off+axis]-smoothed[axis]);
				dst[axis+off]=smoothed[axis];
			}
		}
		}
		else {
			for (int axis=0;axis<sampleSize;axis++) {
				dst[axis+off]=Float.NaN;
			}
		}
		return rc;
	}
	
	public void setTimeConstant(float timeConstant) {
		this.timeConstant=timeConstant;
	}

}
