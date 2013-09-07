package lejos.nxt;

/**
 * Basic interface for EV3 sensor ports.
 * @author andy
 *
 */
public interface SensorPort extends BasicSensorPort, EV3SensorConstants   {
    // TODO: The following do not really work with the new way of doing things
    // but there is code everywhere that uses this interface
    public static final SensorPort S1 = LocalEV3.S1();
    public static final SensorPort S2 = LocalEV3.S2();
    public static final SensorPort S3 = LocalEV3.S3();
    public static final SensorPort S4 = LocalEV3.S4();
    
    /**
     * Open the port and make it available for use.
     * @param port the port number to open
     */    
    public boolean open(int port);
    
    /**
     * Close the port, the port can not be used after this call.
     */
    public void close();
    
    /**
     * Return the id/port number for this port.
     * @return
     */
	public int getId();
	
   /**
     * Set the port pins up ready for use.
     * @param mode The EV3 pin mode
     */
    public void setPinMode(int mode);
	
}
