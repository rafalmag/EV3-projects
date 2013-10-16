package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.Color;
import lejos.robotics.ColorDetector;
import lejos.robotics.LampLightDetector;
import lejos.robotics.LightDetector;

/**
 * Basic sensor driver for the Lego EV3 color sensor <BR>
 * TODO: Add extra methods to make it more compatible with the NXT, check SWITCH_DELAY
 * @author andy
 *
 */
public class EV3ColorSensor extends UARTSensor implements LampLightDetector, LightDetector, ColorDetector
{
    protected static int[] colorMap =
    {
        Color.NONE, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE, Color.BROWN
    };
    protected static final int SWITCH_DELAY = 250;
    
    protected static final int COL_REFLECT = 0;
    protected static final int COL_AMBIENT = 1;
    protected static final int COL_COLOR = 2;
    protected static final int COL_REFRAW = 3;
    protected static final int COL_RGBRAW = 4;
    protected static final int COL_CAL = 5;
    
    protected int light = Color.RED;
    

    public EV3ColorSensor(UARTPort port)
    {
        super(port);
    }

    public EV3ColorSensor(Port port)
    {
        super(port);
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getLightValue()
    {
        switchMode((light == Color.RED ? COL_REFLECT : COL_AMBIENT), SWITCH_DELAY);
        return port.getByte() & 0xff;
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getNormalizedLightValue()
    {
        // TODO Auto-generated method stub
        return getLightValue();
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getHigh()
    {
        // TODO Auto-generated method stub
        return 100;
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getLow()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** {@inheritDoc}
     */    
    @Override
    public Color getColor()
    {
        /*
         * TODO: Understand why this code does not work. It seems to send the sensor
         * into some sort of reset.
        short [] vals = new short[3];
        switchMode(COL_RGBRAW, SWITCH_DELAY);
        port.getShorts(vals, 0, 3);
        switchMode(COL_COLOR, SWITCH_DELAY);
        System.out.println("Red " + vals[0] + " Green " + vals[1] + " blue " + vals[2]);
        // TODO: confirm that the scale factor below is correct
        return new Color(vals[0]*255/1022,vals[1]*255/1022,vals[2]*255/1022, colorMap[port.getByte()] );
        */
        switchMode(COL_COLOR, SWITCH_DELAY);
        return new Color(0, 0, 0, colorMap[port.getByte()] );
        
    }

    /**
     * Return a Color Object that contains the raw color readings.
     * @return Raw Color data (Note the color Id is always Color.NONE)
     */
    public Color getRawColor()
    {
        /*
        short [] vals = new short[3];
        switchMode(COL_RGBRAW, SWITCH_DELAY);
        port.getShorts(vals, 0, 3);
        System.out.println("Raw Red " + vals[0] + " Green " + vals[1] + " blue " + vals[2]);
        // TODO: confirm that the scale factor below is correct
        return new Color(vals[0]*255/1022,vals[1]*255/1022,vals[2]*255/1022, Color.NONE);
        */
        return new Color(0, 0, 0, Color.NONE);
        
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getColorID()
    {
        switchMode(COL_COLOR, SWITCH_DELAY);
        return colorMap[port.getByte()];
    }

    /** {@inheritDoc}
     */    
    @Override
    public void setFloodlight(boolean floodlight)
    {
        light = (floodlight ? Color.RED : Color.BLUE);
        switchMode((light == Color.RED ? COL_REFLECT : COL_AMBIENT), SWITCH_DELAY);
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean isFloodlightOn()
    {
        // TODO Auto-generated method stub
        return light == Color.RED;
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getFloodlight()
    {
        // TODO Auto-generated method stub
        return light;
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setFloodlight(int color)
    {
        int mode;
        light = color;
        switch (color)
        {
        case Color.BLUE:
            mode = COL_AMBIENT;
            break;
        case Color.WHITE:
            mode = COL_COLOR;
            break;
        case Color.RED:
            mode = COL_REFLECT;
            break;
        default:
            // TODO: Should we ignore a wrong color or throw an exception?
            throw new IllegalArgumentException("Invalid color specified");
        }
        switchMode(mode, SWITCH_DELAY);
        // TODO Auto-generated method stub
        return true;
    }

}
