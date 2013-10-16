package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.*;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */
/**
 * LEGO Color Sensor driver.
 * This driver provides access to the LEGO Color sensor. It allows the reading of
 * raw and processed color values. The sensor has a tri-color LED and this can
 * be set to output red/green/blue or off. It also has a full mode in which
 * four samples are read (off/red/green/blue) very quickly. These samples can
 * then be combined using the calibration data provided by the device to
 * determine the "LEGO" color currently being viewed.
 * @author andy
 */
public class ColorSensor extends AnalogSensor implements LightDetector, LampLightDetector, ColorDetector, SensorConstants
{

    protected static int[] colorMap =
    {
        -1, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE
    };
    protected int type;
    private int zero = 1023;
    private int hundred = 0;
    private short[] ADRaw = new short[5];
    private int lampColor = Color.NONE;

    /**
     * Extended color class, that includes the background reading at
     * the time that the other readings were made.
     */
    static public class Color extends lejos.robotics.Color
    {

        private int background;

        public Color(int red, int green, int blue, int background, int colorId)
        {
            super(red, green, blue, colorId);
            this.background = background;
        }

        /**
         * Return the background light level reading
         * @return the background light level
         */
        public int getBackground()
        {
            return background;
        }
    }

    /**
     * Create a new Color Sensor instance and bind it to a port.
     * @param port Port to use for the sensor.
     */
    public ColorSensor(AnalogPort port)
    {
        this(port, Color.WHITE);
    }

    /**
     * Create a new Color Sensor instance and bind it to a port. Set the
     * floodlight to the specified color.
     * @param port Port to use for the sensor.
     * @param color The floodlight color.
     */
    public ColorSensor(AnalogPort port, int color)
    {
        super(port);
        setFloodlight(color);
    }
    /**
     * Create a new Color Sensor instance and bind it to a port.
     * @param port Port to use for the sensor.
     */
    public ColorSensor(Port port)
    {
        this(port, Color.WHITE);
    }

    /**
     * Create a new Color Sensor instance and bind it to a port. Set the
     * floodlight to the specified color.
     * @param port Port to use for the sensor.
     * @param color The floodlight color.
     */
    public ColorSensor(Port port, int color)
    {
        super(port);
        setFloodlight(color);
    }

    /**
     * Change the type of the sensor
     * @param type new sensor type.
     */
    protected void setType(int type)
    {
        if (type != this.type)
        {
            port.setPinMode(type);
            this.type = type;
        }
    }
    
    protected void readRaw()
    {
        port.getShorts(ADRaw, 0, ADRaw.length-1);
    }

    protected void readFull()
    {
        port.getShorts(ADRaw, 0, ADRaw.length);
    }

    /**
     * Return the calibrated light reading.
     * @return Calibrated value as a percentage
     */
    public int getLightValue()
    {
        int val;
        readRaw();
        if (this.type == TYPE_COLORFULL)
        {
            val = (ADRaw[RED_INDEX] + ADRaw[BLUE_INDEX] + ADRaw[GREEN_INDEX]) / 3;
        } else
            val = ADRaw[RED_INDEX + this.type - TYPE_COLORRED];
        return val;
    }

    /**
     * Return the normalized light level.
     * @return The normalized light value.
     */
    public int getNormalizedLightValue()
    {
        // This is the same as the raw value
        return getRawLightValue();
    }

    /**
     * Return the Raw light reading.
     * @return Raw light reading 0-1023
     */
    public int getRawLightValue()
    {
        int val;
        readRaw();
        if (this.type == TYPE_COLORFULL)
        {
            val = (ADRaw[RED_INDEX] + ADRaw[BLUE_INDEX] + ADRaw[GREEN_INDEX]) / 3;
        } else
            val = ADRaw[RED_INDEX + this.type - TYPE_COLORRED];
        return val;
    }

    public void setFloodlight(boolean floodlight)
    {
        setFloodlight(floodlight ? Color.RED : Color.NONE);
    }

    /**
     * Return a Color Object that contains the calibrated color readings.
     * @return Color data
     */
    public ColorSensor.Color getColor()
    {
        setType(TYPE_COLORFULL);
        readFull();
        return new Color(ADRaw[RED_INDEX], ADRaw[GREEN_INDEX], ADRaw[BLUE_INDEX], ADRaw[BLANK_INDEX], colorMap[ADRaw[BLANK_INDEX+1]]);
    }

    /**
     * Return a Color Object that contains the raw color readings.
     * @return Raw Color data (Note the color Id is always Color.NONE)
     */
    public ColorSensor.Color getRawColor()
    {
        setType(TYPE_COLORFULL);
        readRaw();
        return new Color(ADRaw[RED_INDEX], ADRaw[GREEN_INDEX], ADRaw[BLUE_INDEX], ADRaw[BLANK_INDEX], Color.NONE);
    }

    public int getFloodlight()
    {
        return lampColor;
    }

    public boolean isFloodlightOn()
    {
        return (lampColor != Color.NONE);
    }

    public boolean setFloodlight(int color)
    {
        switch (color)
        {
            case Color.RED:
                lampColor = color;
                setType(ColorSensor.TYPE_COLORRED);
                break;
            case Color.BLUE:
                lampColor = color;
                setType(ColorSensor.TYPE_COLORBLUE);
                break;
            case Color.GREEN:
                lampColor = color;
                setType(ColorSensor.TYPE_COLORGREEN);
                break;
            case Color.NONE:
                lampColor = color;
                setType(ColorSensor.TYPE_COLORNONE);
                break;
            case Color.WHITE:
                lampColor = color;
                setType(ColorSensor.TYPE_COLORFULL);
                break;
            default:
                return false;
        }
        return true;
    }

    // TODO: Since this calibrate code (and other code) is the same for every sensor, perhaps we should consider abstract classes to inherit shared code from
    /**
     * call this method when the light sensor is reading the low value - used by readValue
     **/
    public void calibrateLow()
    {
        zero = port.readRawValue();
    }

    /**
     *call this method when the light sensor is reading the high value - used by readValue
     */
    public void calibrateHigh()
    {
        hundred = port.readRawValue();
    }

    /**
     * set the normalized value corresponding to readValue() = 0
     * @param low the low value
     */
    public void setLow(int low)
    {
        zero = 1023 - low;
    }

    /**
     * set the normalized value corresponding to  readValue() = 100;
     * @param high the high value
     */
    public void setHigh(int high)
    {
        hundred = 1023 - high;
    }

    /**
     * return  the normalized value corresponding to readValue() = 0
     */
    public int getLow()
    {
        return 1023 - zero;
    }

    /**
     * return the normalized value corresponding to  readValue() = 100;
     */
    public int getHigh()
    {
        return 1023 - hundred;
    }

    /**
     * Read the current color and return an enumeration constant. This is usually only accurate at a distance
     * of about 1 cm.
     * @return The color id under the sensor.
     */
    public int getColorID()
    {
        setType(TYPE_COLORFULL);
        readFull();
        return colorMap[ADRaw[BLANK_INDEX+1]];
    }
}
