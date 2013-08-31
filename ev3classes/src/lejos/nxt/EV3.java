package lejos.nxt;

public interface EV3
{
    /**
     * return a sensor port object for the specified port. Note that the actual
     * type of sensor port will be automatically determined. The sensor port will
     * be open and ready for use. If the port is in use or it is not possible
     * to open the port return null
     * @param port The port to obtain the sensor object for.
     * @return the sensor port object or null
     */
    public SensorPort openSensorPort(int port);
    /**
     * return a sensor port object for the specified port. typ specifies the typ of
     * port to open. The sensor port will
     * be open and ready for use. If the port is in use or it is not possible
     * to open the port return null
     * @param port The port to obtain the sensor object for.
     * @return the sensor port object or null
     */
    public SensorPort openSensorPort(int port, int typ);
    /**
     * return a new sensor port object. typ specifies the connection type of
     * port to return. The sensor port will
     * not be open. 
     * @param port The port to obtain the sensor object for.
     * @return the sensor port object or null
     */
    public SensorPort newSensorPort(int typ);
    
    /**
     * returns a sensor port object. If the port is already open this method will
     * return the existing port object. If the port is closed a new object will be 
     * created and opened. If there is some problem opening the port null will be
     * returned.
     * @param port The port to obtain the object for
     * @return the sensor port object or null
     */
    public SensorPort getSensorPort(int port);
    
    //TODO: Extend to include motor ports and perhaps motors, also buttons, sound, LCD etc.

}
