package lejos.nxt;
import lejos.robotics.RangeFinder;

/**
 * Basic sensor driver for the Lego EV3 Ultrasonic sensor.<br>
 * TODO: Need to implement other modes. Consider implementing other methods
 * to allow this device to be used in place of the NXT device (getDistance) etc.
 * @author andy
 *
 */
public class EV3UltrasonicSensor extends UARTSensor implements RangeFinder
{

    /**
     * Create the sensor class. The sensor will be set to return measurements in CM.
     * @param port
     */
    public EV3UltrasonicSensor(UARTPort port)
    {
        super(port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getRange()
    {
        return (float)port.getShort()/10.0f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[] getRanges()
    {
        // TODO Work out how to use other modes, maybe change this
        float [] result = new float[1];
        result[0] = getRange();
        return result;
    }

}
