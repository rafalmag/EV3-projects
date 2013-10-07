package lejos.hardware.sensor;

import lejos.hardware.Device;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Base class for analog sensor drivers
 * @author andy
 *
 */
public class AnalogSensor extends Device
{
    protected AnalogPort port;
    
    public AnalogSensor(AnalogPort p)
    {
        this.port = p;
    }
    
    public AnalogSensor(Port p)
    {
        this(p.open(AnalogPort.class));
        releaseOnClose(this.port);
    }
    

}
