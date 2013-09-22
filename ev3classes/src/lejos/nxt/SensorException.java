package lejos.nxt;

/**
 * Exception thrown when errors are detected in a sensor device state.
 * @author andy
 *
 */
public class SensorException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5846698127613306496L;

    public SensorException()
    {
    }

    public SensorException(String message)
    {
        super (message);
    }

    public SensorException(Throwable cause)
    {
        super (cause);
    }

    public SensorException(String message, Throwable cause)
    {
        super (message, cause);
    }
}
