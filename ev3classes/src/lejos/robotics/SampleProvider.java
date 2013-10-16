package lejos.robotics;

/** Abstraction for classes that fetch samples form a sensor and classes that are able to process a sample.<br>   
 * A sample is a measurement taken by a sensor at a single moment in time and consists of one or more elements. 
 * The number of elements in a sample is specific to a sensor and the mode it is in.
 * @author Aswin Bouwmeester
 *
 */
public interface SampleProvider {
	
	/** Returns the number of elements in a sample.<br>
	 * The number of elements does not change during runtime.
	 * @return
	 * the number of elements in a sample
	 */
	public int sampleSize();
	
	/** Fetches a sample from a sensor or filter.
	 * @param sample
	 * The array to store the sample in. 
	 * @param offset
	 * The elements of the sample are stored in the array starting at the offset position.
	 * @return
	 * 0, on succes <br>
	 * non-zero, when the sample could not be fetched or processed
	 */
	public int fetchSample(float[] sample, int offset);
}
