package lejos.hardware.lcd;

public interface CommonLCD {
	
    /**
     * Common raster operations for use with bitBlt
     */
    public static final int ROP_CLEAR = 0x00000000;
    public static final int ROP_AND = 0xff000000;
    public static final int ROP_ANDREVERSE = 0xff00ff00;
    public static final int ROP_COPY = 0x0000ff00;
    public static final int ROP_ANDINVERTED = 0xffff0000;
    public static final int ROP_NOOP = 0x00ff0000;
    public static final int ROP_XOR = 0x00ffff00;
    public static final int ROP_OR = 0xffffff00;
    public static final int ROP_NOR = 0xffffffff;
    public static final int ROP_EQUIV = 0x00ffffff;
    public static final int ROP_INVERT = 0x00ff00ff;
    public static final int ROP_ORREVERSE = 0xffff00ff;
    public static final int ROP_COPYINVERTED = 0x0000ffff;
    public static final int ROP_ORINVERTED = 0xff00ffff;
    public static final int ROP_NAND = 0xff0000ff;
    public static final int ROP_SET = 0x000000ff;

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
