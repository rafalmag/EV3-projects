package lejos.nxt;

import lejos.robotics.Touch;

/**
 * Basic sensor driver for the Lego EV3 Touch sensor
 * @author andy
 *
 */
public class EV3TouchSensor extends AnalogSensor implements Touch
{
    
    public EV3TouchSensor(AnalogPort port)
    {
        super(port);
    }

    public EV3TouchSensor(Port port)
    {
        super(port);
    }
    
    @Override
    public boolean isPressed()
    {
        return port.getPin6() > EV3SensorConstants.ADC_RES/2;
    }

}
