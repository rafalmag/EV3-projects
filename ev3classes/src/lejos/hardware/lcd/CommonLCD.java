package lejos.hardware.lcd;

public interface CommonLCD {

    /**
     * Refresh the display. If auto refresh is off, this method will wait until
     * the display refresh has completed. If auto refresh is on it will return
     * immediately.
     */
    public void refresh();
    
    /**
     * Clear the display.
     */
    public void clear();
    
    /**
     * Return the width of the associated drawing surface.
     * <br><b>Note</b>: This is a non standard method.
     * @return width of the surface
     */
    public int getWidth();

    /**
     * Return the height of the associated drawing surface.
     * <br><b>Note</b>: This is a non standard method.
     * @return height of the surface.
     */
    public int getHeight();
    
    /**
     * Provide access to the LCD display frame buffer. 
     * @return byte array that is the frame buffer.
     */
    public byte[] getDisplay();
    
    /**
     * Get access to hardware LCD display.
     * @return byte array that is the frame buffer
     */
    public byte[] getHWDisplay();
    
    /**
     * Set the LCD contrast.
     * @param contrast 0 blank 0x60 full on
     */
    public void setContrast(int contrast);
}
