package lejos.nxt;


public interface UARTPort extends SensorPort
{

    /**
     * read a single byte from the device
     * @return the byte value
     */
    public byte getByte();

    /**
     * read a number of bytes from the device
     * @param vals byte array to accept the data
     * @param offset offset at which to store the data
     * @param len number of bytes to read
     */
    public void getBytes(byte [] vals, int offset, int len);

    /**
     * read a single short from the device.
     * @return the short value
     */
    public int getShort();
   
    /**
     * read a number of shorts from the device
     * @param vals short array to accept the data
     * @param offset offset at which to store the data
     * @param len number of shorts to read
     */
    public void getShorts(short [] vals, int offset, int len);

    /**
     * Get the string name of the specified mode.<p><p>
     * TODO: Make other mode data available.
     * @param mode mode to lookup
     * @return String version of the mode name
     */
    public String getModeName(int mode);

    /**
     * Return the current sensor reading to a string. 
     */
    public String toString();


}
