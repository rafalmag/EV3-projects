package lejos.nxt;

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
