package lejos.robotics.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import lejos.robotics.SampleProvider;

/**
 * This filter is used to calibrate sensors for offset and scale errors using
 * linear interpolation. <br>
 * The filter has two modes of operation. In operational mode it corrects
 * samples coming from the sensor. In calibration mode the filter calculates
 * calibration parameters.
 * <p>
 * 
 * <b>Operational mode</b><br>
 * In operational mode the filter corrects incoming samples for offset and scale
 * errors. The correction algorithm can uses calibration parameters for this.
 * These calibration parameters can be computed in calibration mode. They can
 * also be loaded from the filesystem using the load() method.
 * <p>
 * 
 * <b>How it works</b><br>
 * The correction algorithm uses two calibration parameters, offset and scale.<br>
 * The offset parameter corrects for offset errors. An offset error results in a
 * sample being a fixed amount off the truth: 2 becomes 3, 6 becomes 7. It is
 * corrected by subtracting a constant value (1 in this example) from the
 * sample, the offset error. <br>
 * The scale parameter corrects for scale errors. As a result of a scale error a
 * sample is always a fixed percentage off the truth: 2 becomes 3, 6 becomes 9.
 * It is corrected by dividing the sample with a constant value, the scale
 * error, 1.5 in this case. <br>
 * The combined correction formula is correctedValue = ( rawValue - offsetError
 * ) / scaleError. This calibration technique works on all sensors who's output
 * is linear to the input.<br>
 * If no correction parameters are calculated or loaded the filter uses 0 for
 * offset correction and 1 for scale correction.
 * <p>
 * 
 * <b>How to use the filter in operational mode</b><br>
 * In operational mode is the default mode. It is always active while the filter
 * is not calibrating. <br>
 * <p>
 * 
 * 
 * 
 * <b>Calibration mode</b><br>
 * In calibration mode the two calibration parameters are calculated. This is
 * done by comparing samples to a user specified reference value and/or range.
 * Once calibration parameters are calculated they can be used immediately or
 * stored to the filesystem for later use using the store() method.
 * <p>
 * 
 * <b>How it works</b><br>
 * The CalibratorFiltersupports both offset and scale calibration. However both
 * are optional and can be enabled or disabled. During calibration the filter
 * collects minimum and maximum sample values. From this it calculates offset
 * (as the average of the minimum and maximum value corrected for the reference
 * value) and scale (as the difference between maximum and minimum value scaled
 * for range). To minimize the effect of sensor noise or sensor movements during
 * calculation sample values are low-passed before they are being used for
 * calibration.
 * <p>
 * 
 * <b>How to use the filter in calibration mode</b><br>
 * Calibration is started using the startCalibration method and ended with the
 * endCalibration method. During calibration the program that uses the filter
 * must fetch samples to collect data for the calibration algorithm. At the end
 * the calibration process the calculated calibration settings can be stored
 * using the store(filename) method. Calibration can be paused if needed.
 * <p>
 * 
 * <b>How to tune the calibration process</b><br>
 * There are three important parameters to the calibration process that can be
 * modified by the user program.
 * <ul>
 * <li>
 * The reference value. This is the expected output of the sensor. For
 * calibrating a (motionless) gyro sensor this will be 0. For calibrating a
 * range sensor for example this should be the range to the object the sensor is
 * calibrated to. The reference value is used in calculating the offset
 * parameter, it is not used in calculating scale. The reference has a default
 * value of 0.</li>
 * <li>
 * The range value. This is the expected range of the sensor output. For
 * calibrating an accelerometer this could be 2*9.81 when the output should be
 * in m/s^2. The range value is used in calculating the scale parameter, it is
 * not used in calculating offset. The range has a default value of 2, meaning
 * sample values are normalized to a range of -1 to 1.</li>
 * <li>
 * The timeConstant value. This is the timeConstant value of a low-pass filter
 * that is used in calibration mode. A low pass filter is used during
 * calibration to for two reasons. First to overcome the effects of sensor noise
 * that could otherwise seriously affect range calculation. Secondly it filters
 * out the side effect of user manipulation when turning the sensor as part of
 * the calibration process (six way tumbling method). The parameter affects the
 * amount of smoothing of the low-pass filter. The higher the value, the
 * smoother the samples. Smoother samples are less affected by sensor noise or
 * external shocks but take longer to settle. The time constant has a default
 * value of 0, meaning no smoothing is done by default.</li>
 * </ul>
 * 
 * @author Aswin Bouwmeester
 * 
 */
public class LinearCalibrationFilter extends AbstractFilter {
  private boolean                calibrateForScale  = false;
  private boolean                calibrateForOffset = true;
  private float[]                reference;
  private float[]                range;
  private float[]                min;
  private float[]                max;
  private float[]                offset;
  private float[]                scale;
  private boolean                calibrationMode    = false;
  private final FilterProperties props              = new FilterProperties();
  private LowPassFilter          lowPassFilter      = null;

  /** Construcor
   * @param source
   * SampleProvider
   * @param filename
   * Filename to load calibration settings from
   */
  public LinearCalibrationFilter(SampleProvider source, String filename) {
    this(source);
    load(filename);
  }

  public LinearCalibrationFilter(SampleProvider source) {
    super(source);
    lowPassFilter = new LowPassFilter(source, 0);
    reference = new float[sampleSize];
    range = new float[sampleSize];
    min = new float[sampleSize];
    max = new float[sampleSize];
    offset = new float[sampleSize];
    scale = new float[sampleSize];

    for (int i = 0; i < sampleSize; i++) {
      reference[i] = 0;
      range[i] = 2;
      offset[i] = 0;
      scale[i] = 1;
    }
  }

  /**
   * Disables or enables scale calibration. By default scale calibration is
   * disabled.
   * 
   * @param calibrate
   *          A True value means that the sensor will be calibrated for scale when calibrating
   */
  public void calibrateForScale(boolean calibrate) {
    calibrateForScale = calibrate;
  }

  /**
   * Disables or enables offset calibration. By default offset calibration is
   * enabled.
   * 
   * @param calibrate
   *          A True value means that the sensor will be calibrated for offset when calibrating
   */
  public void calibrateForOffset(boolean calibrate) {
    calibrateForOffset = calibrate;
  }

  /** Sets a reference value for offset calibration. 
   * @param referenceValue
   *          Sets the reference value of all elements in the sample to the same value.
   */
  public void setReference(float referenceValue) {
    for (int i = 0; i < sampleSize; i++)
      reference[i] = referenceValue;
  }

  /** Sets a reference value for offset calibration. <br>
   * 
   * For calibrating a motionless accelerometer for offset this array could be
   * {0, 0, 9.81f} as the sensor should not experience gravity over the X and Y
   * axes and 1G (9.81 m/s^2) over the Z axis.
   * 
   * @param referenceValues
   *          An array with reference values.
   */
  public void setReference(float[] referenceValues) {
    System.arraycopy(referenceValues, 0, reference, 0, sampleSize);
  }

  /**
   * Sets the expected range when calibrating for
   * scale. <br>
   * For calibrating an accelerometer using the six way tumble method
   * this value could be 19.62 m/s^2 (equals 2G).
   * 
   * @param rangeValue
   *          The range value to be used.
   */
  public void setRange(float rangeValue) {
    for (int i = 0; i < sampleSize; i++)
      range[i] = rangeValue;
  }

  /**
   * Sets the expected range of all elements in the samples individually when
   * calibrating for scale.
   * 
   * @param rangeValues
   *          The range value to be used.
   */
  public void setRange(float[] rangeValues) {
    System.arraycopy(rangeValues, 0, range, 0, sampleSize);
  }

  /** Sets the time constant for the lowpass filter that is used when calibrating. <br>
   * A value of zero will effectivly disable the lowpass filter. 
   * Higher values will remove more noise from the signal and give better results, especially when calibraating for scale. 
   * The downside of higher timeConstants is that calibrating takes more time. 
   * @param timeConstant
   * between 0 and 1
   */
  public void setTimeConstant(float timeConstant) {
    lowPassFilter.setTimeConstant(timeConstant);
  }

  /**Returns an array with the offset correction paramaters that are currently in use
   * @return
   */
  public float[] getOffsetCorrection() {
    float[] ret = new float[sampleSize];
    System.arraycopy(offset, 0, ret, 0, sampleSize);
    return ret;
  }

  /**Returns an array with the scale correction paramaters that are currently in use
   * @return
   */
  public float[] getScaleCorrection() {
    float[] ret = new float[sampleSize];
    System.arraycopy(scale, 0, ret, 0, sampleSize);
    return ret;
  }

  /**
   * Starts a calibration proces. Resets collected minimum and maximum values.
   * After starting calibration new minimum and maximum values are calculated on
   * each fetched sample. From this updated offset and scale parameters are
   * calculated.
   */
  public void startCalibration() {
    calibrationMode = true;
    for (int i = 0; i < sampleSize; i++) {
      min[i] = Float.POSITIVE_INFINITY;
      max[i] = Float.NEGATIVE_INFINITY;
      offset[i] = 0;
      scale[i] = 1;
    }
  }

  /**
   * Halts the process of updating calibration parameters.
   */
  public void stopCalibration() {
    calibrationMode = false;
  }

  /**
   * Resumes the process of updating calibration parameters after a stop.
   */
  public void resumeCalibration() {
    calibrationMode = true;
  }

  /**
   * Stores the calibration parameters, offset and/or scale depending on current
   * settings, to a filterProperties file. Stored parameters can later be used
   * by the CalibrationFilter.
   * 
   * @param name
   *          A name to use for storing calibration parameters
   */
  public void store(String filename) {
    props.load(filename);
    if (calibrateForOffset)
      props.setProperty("offset", offset);
    if (calibrateForScale)
      props.setProperty("scale", scale);
    props.store(filename);
  }

  public void load(String name) {

    for (int i = 0; i < sampleSize; i++) {
      offset[i] = 0;
      scale[i] = 1;
    }
    props.load(name);
    offset = props.getProperty("offset", offset);
    scale = props.getProperty("scale", scale);
  }

  /**
   * Fetches a sample from the sensor and updates calibration parameters when
   * the calibration process is running.
   */
  public void fetchSample(float[] dst, int off) {
    if (!calibrationMode) {
      source.fetchSample(dst, off);
      for (int i = 0; i < sampleSize; i++)
        dst[i + off] = (dst[i + off] - offset[i]) / scale[i];
    }
    else {
      lowPassFilter.fetchSample(dst, off);
      for (int i = 0; i < sampleSize; i++) {
        if (min[i] > dst[i + off])
          min[i] = dst[i + off];
        if (max[i] < dst[i + off])
          max[i] = dst[i + off];
        if (calibrateForOffset)
          offset[i] = min[i] + (max[i] - min[i]) / 2 - reference[i];
        if (calibrateForScale)
          scale[i] = (max[i] - min[i]) / (range[i]);
        //dst[i + off] = (dst[i + off] - offset[i]) / scale[i];
      }
    }
  }

  private class FilterProperties {
    private final static String DIRECTORY = "/home/root/sensorCalibration/";
    private final static String EXT       = ".cal";
    private final Properties    props     = new Properties();
    private String              raw;

    private File getFile(String filename) {
      return new File(DIRECTORY + filename + EXT);
    }

    public void load(String filename) {
      try {
        new File(DIRECTORY).mkdir();
        File f = getFile(filename);
        f.createNewFile();
        FileInputStream in = new FileInputStream(f);
        props.load(in);
        in.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void store(String filename) {
      try {
        new File(DIRECTORY).mkdir();
        File f = getFile(filename);
        f.createNewFile();
        FileOutputStream out = new FileOutputStream(f);
        props.store(out, "Parameters for sensor calibration");
        out.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    public float[] getProperty(String key, float[] defaultValues) {
      raw = props.getProperty(key);
      if (raw == null)
        return defaultValues;
      StringTokenizer tokenizer = new StringTokenizer(raw, " ");
      int n = tokenizer.countTokens();
      if (n!=sampleSize) {
        // TODO: raise an exception?
        return defaultValues;
      }
      float[] values = new float[n];
      for (int i = 0; i < n; i++) {
        values[i] = Float.parseFloat(tokenizer.nextToken());
      }
      return values;
    }

    public void setProperty(String key, float[] values) {
      StringBuilder builder = new StringBuilder();
      int n = values.length;
      for (int i = 0; i < n; i++) {
        if (i != 0)
          builder.append(" ");
        builder.append(values[i]);
      }
      props.setProperty(key, builder.toString());
    }

  }

}
