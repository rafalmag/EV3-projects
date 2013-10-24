package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.robotics.*;

/**
 * LEGO NXT Color Sensor driver.
 * This driver provides access to the LEGO Color sensor. It allows the reading of
 * raw color values. The sensor has a tri-color LED and this can
 * be set to output red/green/blue or off. It also has a full mode in which
 * four samples are read (off/red/green/blue) very quickly. These samples can
 * then be combined using the calibration data provided by the device to
 * determine the "LEGO" color currently being viewed.
 * @author andy
 */
public class NXTColorSensor extends AnalogSensor implements SensorConstants, SensorMode, LampController, ColorIdentifier
{

    protected static int[] colorMap =
    {
        -1, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE
    };
    protected int type;
    private short[] ADRaw = new short[5];

    private class ModeProvider implements SensorMode
    {
        final String name;
        final int type;
        final int sampleSize;
        final int startOffset;
        
        ModeProvider(String name, int typ, int sz, int offset)
        {
            this.name = name;
            type = typ;
            sampleSize = sz;
            startOffset = offset;
        }

        @Override
        public int sampleSize()
        {
            return sampleSize;
        }

        @Override
        public void fetchSample(float[] sample, int offset)
        {
            setType(type);
            readRaw();
            for(int i = 0; i < sampleSize; i++)
                sample[offset+i] = ADRaw[startOffset+i];
        }

        @Override
        public String getName()
        {
            return name;
        }
        
    }
    
    protected void init()
    {
        setModes(new SensorMode[]{getColorIDMode(), getRedMode(), getGreenMode(), getBlueMode(), getRGBMode(), getAmbientMode() });        
        setFloodlight(Color.WHITE);
    }
    
    
    /**
     * Create a new Color Sensor instance and bind it to a port.
     * @param port Port to use for the sensor.
     */
    public NXTColorSensor(AnalogPort port)
    {
        super(port);
        init();
    }

    /**
     * Create a new Color Sensor instance and bind it to a port.
     * @param port Port to use for the sensor.
     */
    public NXTColorSensor(Port port)
    {
        super(port);
        init();
    }

    /**
     * Change the type of the sensor
     * @param type new sensor type.
     */
    protected void setType(int type)
    {
        if (type != this.type)
        {
            // Note we use type to allow this sensor driver to work with a remote NXT
            port.setType(type);
            this.type = type;
        }
    }

    /**
     * get a sample provider in color ID mode
     * @return the sample provider
     */
    public SensorMode getColorIDMode()
    {
        return this;
    }

    /**
     * get a sample provider the returns the light value when illuminated with a
     * Red light source.
     * @return the sample provider
     */
    public SensorMode getRedMode()
    {
        return new ModeProvider("Red", TYPE_COLORRED, 1, 0);
    }

    /**
     * get a sample provider the returns the light value when illuminated with a
     * Green light source.
     * @return the sample provider
     */
    public SensorMode getGreenMode()
    {
        return new ModeProvider("Green", TYPE_COLORGREEN, 1, 1);
    }

    /**
     * get a sample provider the returns the light value when illuminated with a
     * Blue light source.
     * @return the sample provider
     */
    public SensorMode getBlueMode()
    {
        return new ModeProvider("Blue", TYPE_COLORBLUE, 1, 2);
    }

    /**
     * get a sample provider the returns the light value when illuminated without a
     * light source.
     * @return the sample provider
     */
    public SensorMode getAmbientMode()
    {
        return new ModeProvider("None", TYPE_COLORNONE, 1, 3);
    }
    
    /**
     * get a sample provider the returns the light values (RGB + ambient) when illuminated by a
     * white light source.
     * @return the sample provider
     */
    public SensorMode getRGBMode()
    {
        return new ModeProvider("RGB", TYPE_COLORFULL, 4, 0);
    }
    
    protected void readRaw()
    {
        port.getShorts(ADRaw, 0, ADRaw.length-1);
    }

    protected void readFull()
    {
        port.getShorts(ADRaw, 0, ADRaw.length);
    }

    public void setFloodlight(boolean floodlight)
    {
        setFloodlight(floodlight ? Color.RED : Color.NONE);
    }


    public int getFloodlight()
    {
        switch(type)
        {
        case TYPE_COLORFULL:
            return Color.WHITE;
        case TYPE_COLORRED:
            return Color.RED;
        case TYPE_COLORGREEN:
            return Color.GREEN;
        case TYPE_COLORBLUE:
            return Color.BLUE;
        case TYPE_COLORNONE:
            return Color.NONE;
        default:
            throw new IllegalStateException("Unknown color type" + type);
        }
    }

    public boolean isFloodlightOn()
    {
        return (getFloodlight() != Color.NONE);
    }

    public boolean setFloodlight(int color)
    {
        switch (color)
        {
            case Color.RED:
                setType(NXTColorSensor.TYPE_COLORRED);
                break;
            case Color.BLUE:
                setType(NXTColorSensor.TYPE_COLORBLUE);
                break;
            case Color.GREEN:
                setType(NXTColorSensor.TYPE_COLORGREEN);
                break;
            case Color.NONE:
                setType(NXTColorSensor.TYPE_COLORNONE);
                break;
            case Color.WHITE:
                setType(NXTColorSensor.TYPE_COLORFULL);
                break;
            default:
                return false;
        }
        return true;
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

    @Override
    public int sampleSize()
    {
        return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset)
    {
        sample[offset] = (float) getColorID();
    }


    @Override
    public String getName()
    {
        return "ColorID";
    }
}
