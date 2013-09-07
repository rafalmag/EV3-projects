package lejos.nxt;

import lejos.robotics.Touch;

/**
 * Basic sensor driver for the Lego EV3 Touch sensor
 * @author andy
 *
 */
public class EV3TouchSensor implements Touch
{
    ADSensorPort port;
    
    public EV3TouchSensor(ADSensorPort port)
    {
        this.port = port;
    }

    @Override
    public boolean isPressed()
    {
        return port.getPin6() > ADSensorPort.ADC_RES/2;
    }

}
