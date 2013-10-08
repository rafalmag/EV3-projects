package lejos.hardware;


import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

import com.sun.jna.Pointer;
/**
 * Provide access to the EV3 LCD display
 *
 */
//public class LCD extends JPanel
public class LCD extends Thread
{
    public static final int SCREEN_WIDTH = 178;
    public static final int SCREEN_HEIGHT = 128;
    public static final int NOOF_CHARS = 96;
    public static final int FONT_WIDTH = 10;
    public static final int FONT_HEIGHT = 16;
    public static final int CELL_WIDTH = FONT_WIDTH;
    public static final int CELL_HEIGHT = FONT_HEIGHT;
    public static final int DISPLAY_CHAR_WIDTH = SCREEN_WIDTH / CELL_WIDTH;
    public static final int DISPLAY_CHAR_DEPTH = SCREEN_HEIGHT / CELL_HEIGHT;
    public static final int DEFAULT_REFRESH_PERIOD = 250;
    private static byte[] font = new byte[]
       {(byte) 0x00, (byte) 0xc0, (byte) 0xc0, (byte) 0x0c, (byte) 0x00, 
        (byte) 0x30, (byte) 0x10, (byte) 0xc6, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60, 
        (byte) 0x78, (byte) 0xc0, (byte) 0x80, (byte) 0x07, (byte) 0x1e, 
        (byte) 0x30, (byte) 0xf8, (byte) 0x07, (byte) 0x9e, (byte) 0x7f, 
        (byte) 0x78, (byte) 0xe0, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3f, 
        (byte) 0xf8, (byte) 0xe0, (byte) 0xe1, (byte) 0x07, (byte) 0x1e, 
        (byte) 0x7e, (byte) 0xf8, (byte) 0xe7, (byte) 0x1f, (byte) 0x1e, 
        (byte) 0x86, (byte) 0xf9, (byte) 0x07, (byte) 0x98, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x18, (byte) 0x1e, 
        (byte) 0x7e, (byte) 0xe0, (byte) 0xe1, (byte) 0x07, (byte) 0x1e, 
        (byte) 0xfe, (byte) 0x19, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0xe6, (byte) 0x1f, (byte) 0x1e, 
        (byte) 0x06, (byte) 0xe0, (byte) 0x01, (byte) 0x03, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
        (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x0f, (byte) 0x00, 
        (byte) 0x06, (byte) 0xc0, (byte) 0x00, (byte) 0x86, (byte) 0x01, 
        (byte) 0x3c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x38, 
        (byte) 0x30, (byte) 0x70, (byte) 0x80, (byte) 0x19, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0xc1, (byte) 0x0c, (byte) 0x33, 
        (byte) 0x30, (byte) 0x38, (byte) 0x62, (byte) 0x06, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x60, 
        (byte) 0xcc, (byte) 0xe0, (byte) 0xc0, (byte) 0x0c, (byte) 0x33, 
        (byte) 0x30, (byte) 0x18, (byte) 0x00, (byte) 0x03, (byte) 0x60, 
        (byte) 0xcc, (byte) 0x30, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x73, 
        (byte) 0x8c, (byte) 0x31, (byte) 0x63, (byte) 0x0c, (byte) 0x33, 
        (byte) 0xc6, (byte) 0x18, (byte) 0x60, (byte) 0x00, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x18, (byte) 0x33, 
        (byte) 0xc6, (byte) 0x30, (byte) 0x63, (byte) 0x0c, (byte) 0x33, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x18, (byte) 0x06, 
        (byte) 0x06, (byte) 0x80, (byte) 0x81, (byte) 0x0f, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
        (byte) 0x80, (byte) 0x01, (byte) 0x80, (byte) 0x19, (byte) 0x00, 
        (byte) 0x06, (byte) 0xc0, (byte) 0x00, (byte) 0x86, (byte) 0x01, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x60, (byte) 0x06, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0xc1, (byte) 0x0c, (byte) 0x33, 
        (byte) 0xfc, (byte) 0x28, (byte) 0x23, (byte) 0x04, (byte) 0x08, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x30, 
        (byte) 0x86, (byte) 0xf9, (byte) 0x60, (byte) 0x98, (byte) 0x61, 
        (byte) 0x18, (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x60, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0xc0, (byte) 0x00, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x06, (byte) 0x1b, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0xc6, (byte) 0x18, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x31, 
        (byte) 0x06, (byte) 0x18, (byte) 0xe6, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x18, (byte) 0x06, 
        (byte) 0x0c, (byte) 0x80, (byte) 0xe1, (byte) 0x1c, (byte) 0x00, 
        (byte) 0x10, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
        (byte) 0x80, (byte) 0x01, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0x01, (byte) 0x00, (byte) 0x33, 
        (byte) 0xb6, (byte) 0x29, (byte) 0x21, (byte) 0x04, (byte) 0x04, 
        (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x30, 
        (byte) 0xc6, (byte) 0xc1, (byte) 0x60, (byte) 0x98, (byte) 0x61, 
        (byte) 0x18, (byte) 0x18, (byte) 0xc0, (byte) 0x00, (byte) 0x60, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0x60, (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x60, 
        (byte) 0x06, (byte) 0x1b, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x39, 
        (byte) 0x06, (byte) 0x38, (byte) 0xe7, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x0c, (byte) 0x06, 
        (byte) 0x0c, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x20, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
        (byte) 0x80, (byte) 0x01, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0x01, (byte) 0x00, (byte) 0x33, 
        (byte) 0xb6, (byte) 0xb9, (byte) 0x61, (byte) 0x06, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, 
        (byte) 0xc6, (byte) 0xc1, (byte) 0x00, (byte) 0x18, (byte) 0x60, 
        (byte) 0x18, (byte) 0x18, (byte) 0xc0, (byte) 0x00, (byte) 0x30, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x70, 
        (byte) 0xe6, (byte) 0x1b, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x01, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x19, 
        (byte) 0x06, (byte) 0x38, (byte) 0xe7, (byte) 0x99, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0xcc, (byte) 0x18, (byte) 0x06, (byte) 0x0c, (byte) 0x06, 
        (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, 
        (byte) 0x80, (byte) 0x01, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x01, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0x01, (byte) 0x80, (byte) 0x7f, 
        (byte) 0x3e, (byte) 0x90, (byte) 0xc0, (byte) 0x03, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0xc1, (byte) 0x0c, (byte) 0x0c, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, 
        (byte) 0xa6, (byte) 0xc1, (byte) 0x00, (byte) 0x18, (byte) 0x60, 
        (byte) 0x0c, (byte) 0xf8, (byte) 0x61, (byte) 0x00, (byte) 0x30, 
        (byte) 0xcc, (byte) 0x18, (byte) 0x06, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x18, (byte) 0xf8, (byte) 0x07, (byte) 0x03, (byte) 0x30, 
        (byte) 0x36, (byte) 0x1b, (byte) 0x66, (byte) 0x8c, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x01, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x0d, 
        (byte) 0x06, (byte) 0x38, (byte) 0x67, (byte) 0x99, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x98, (byte) 0x07, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0xcc, (byte) 0x18, (byte) 0x06, (byte) 0x06, (byte) 0x06, 
        (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xe0, (byte) 0x63, (byte) 0x07, (byte) 0x3e, 
        (byte) 0xb8, (byte) 0xe1, (byte) 0xe1, (byte) 0x0f, (byte) 0x6e, 
        (byte) 0x76, (byte) 0xf0, (byte) 0x80, (byte) 0x87, (byte) 0x61, 
        (byte) 0x30, (byte) 0x38, (byte) 0x63, (byte) 0x07, (byte) 0x1e, 
        (byte) 0x76, (byte) 0xe0, (byte) 0x66, (byte) 0x0e, (byte) 0x3f, 
        (byte) 0xfc, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0xe6, (byte) 0x1f, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0x38, (byte) 0xc0, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0x81, (byte) 0x07, (byte) 0x0c, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0xa6, (byte) 0xc1, (byte) 0x00, (byte) 0x0c, (byte) 0x3e, 
        (byte) 0x0c, (byte) 0x00, (byte) 0xe3, (byte) 0x07, (byte) 0x30, 
        (byte) 0x78, (byte) 0x30, (byte) 0x06, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x18, 
        (byte) 0x36, (byte) 0x1b, (byte) 0xe6, (byte) 0x87, (byte) 0x01, 
        (byte) 0x86, (byte) 0xf9, (byte) 0x63, (byte) 0x80, (byte) 0x01, 
        (byte) 0xfe, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x07, 
        (byte) 0x06, (byte) 0xf8, (byte) 0x67, (byte) 0x9b, (byte) 0x61, 
        (byte) 0xc6, (byte) 0x18, (byte) 0x66, (byte) 0x0c, (byte) 0x0f, 
        (byte) 0x30, (byte) 0x18, (byte) 0xc6, (byte) 0x8c, (byte) 0x6d, 
        (byte) 0x78, (byte) 0x30, (byte) 0x03, (byte) 0x03, (byte) 0x06, 
        (byte) 0x30, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x10, (byte) 0xe6, (byte) 0x0c, (byte) 0x63, 
        (byte) 0xcc, (byte) 0x31, (byte) 0x83, (byte) 0x01, (byte) 0x73, 
        (byte) 0xce, (byte) 0xc0, (byte) 0x00, (byte) 0x86, (byte) 0x31, 
        (byte) 0x30, (byte) 0xf8, (byte) 0xe7, (byte) 0x0c, (byte) 0x33, 
        (byte) 0xce, (byte) 0x10, (byte) 0xe7, (byte) 0x9f, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x6d, 
        (byte) 0x86, (byte) 0x19, (byte) 0x06, (byte) 0x18, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0x70, (byte) 0x40, (byte) 0xc0, (byte) 0x13, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0xe1, (byte) 0x9f, (byte) 0x7f, 
        (byte) 0x00, (byte) 0xf8, (byte) 0x07, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x96, (byte) 0xc1, (byte) 0x00, (byte) 0x06, (byte) 0x60, 
        (byte) 0x6c, (byte) 0x00, (byte) 0x66, (byte) 0x0c, (byte) 0x18, 
        (byte) 0xcc, (byte) 0xe0, (byte) 0x07, (byte) 0x00, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x18, 
        (byte) 0x36, (byte) 0x1b, (byte) 0x66, (byte) 0x8c, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0xe0, (byte) 0x8f, (byte) 0x79, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x07, 
        (byte) 0x06, (byte) 0xd8, (byte) 0x66, (byte) 0x9a, (byte) 0x61, 
        (byte) 0x7e, (byte) 0x18, (byte) 0xe6, (byte) 0x07, (byte) 0x3c, 
        (byte) 0x30, (byte) 0x18, (byte) 0xc6, (byte) 0x8c, (byte) 0x6d, 
        (byte) 0x78, (byte) 0xf0, (byte) 0x83, (byte) 0x01, (byte) 0x06, 
        (byte) 0x30, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x86, (byte) 0x81, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x19, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0xe6, (byte) 0x80, (byte) 0x01, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x6d, 
        (byte) 0xcc, (byte) 0x18, (byte) 0x06, (byte) 0x0c, (byte) 0x06, 
        (byte) 0x30, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x80, (byte) 0x7f, 
        (byte) 0xf0, (byte) 0x61, (byte) 0x62, (byte) 0x1e, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0x81, (byte) 0x07, (byte) 0x0c, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, 
        (byte) 0x96, (byte) 0xc1, (byte) 0x00, (byte) 0x03, (byte) 0x60, 
        (byte) 0x66, (byte) 0x00, (byte) 0x66, (byte) 0x18, (byte) 0x18, 
        (byte) 0x86, (byte) 0x01, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0x0c, (byte) 0xf8, (byte) 0x07, (byte) 0x06, (byte) 0x0c, 
        (byte) 0xb6, (byte) 0xfb, (byte) 0x67, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x0d, 
        (byte) 0x06, (byte) 0xd8, (byte) 0x66, (byte) 0x9e, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x06, (byte) 0x78, 
        (byte) 0x30, (byte) 0x18, (byte) 0xc6, (byte) 0x8c, (byte) 0x6d, 
        (byte) 0xcc, (byte) 0xe0, (byte) 0x81, (byte) 0x01, (byte) 0x06, 
        (byte) 0x60, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xf0, (byte) 0x67, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x86, (byte) 0x81, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x0d, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x00, (byte) 0x07, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x6d, 
        (byte) 0xfc, (byte) 0x18, (byte) 0x06, (byte) 0x06, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0xb6, (byte) 0x21, (byte) 0x27, (byte) 0x0c, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0xc1, (byte) 0x0c, (byte) 0x0c, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, 
        (byte) 0x8e, (byte) 0xc1, (byte) 0x80, (byte) 0x01, (byte) 0x60, 
        (byte) 0x66, (byte) 0x00, (byte) 0x66, (byte) 0x18, (byte) 0x18, 
        (byte) 0x86, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
        (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x66, (byte) 0x1b, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x98, (byte) 0x19, 
        (byte) 0x06, (byte) 0xd8, (byte) 0x66, (byte) 0x9c, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x0c, (byte) 0x60, 
        (byte) 0x30, (byte) 0x18, (byte) 0x86, (byte) 0x04, (byte) 0x33, 
        (byte) 0xcc, (byte) 0xc0, (byte) 0xc0, (byte) 0x00, (byte) 0x06, 
        (byte) 0x60, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0xf9, (byte) 0x87, (byte) 0x81, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x07, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x6d, 
        (byte) 0x30, (byte) 0x18, (byte) 0x06, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0xb6, (byte) 0x31, (byte) 0x25, (byte) 0x04, (byte) 0x00, 
        (byte) 0x18, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
        (byte) 0x8e, (byte) 0xc1, (byte) 0xc0, (byte) 0x80, (byte) 0x61, 
        (byte) 0x66, (byte) 0x18, (byte) 0x66, (byte) 0x18, (byte) 0x0c, 
        (byte) 0x86, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x60, (byte) 0x98, (byte) 0x39, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x9c, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x8c, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x86, (byte) 0x07, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0xc0, (byte) 0x00, (byte) 0x06, 
        (byte) 0xc0, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x80, (byte) 0x81, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x0f, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x00, (byte) 0x38, 
        (byte) 0x30, (byte) 0x18, (byte) 0xc6, (byte) 0x8c, (byte) 0x6d, 
        (byte) 0xfc, (byte) 0x30, (byte) 0x83, (byte) 0x01, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0xfc, (byte) 0x10, (byte) 0x25, (byte) 0x0c, (byte) 0x00, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0xfe, (byte) 0x19, (byte) 0x66, (byte) 0x18, (byte) 0x0c, 
        (byte) 0x86, (byte) 0x81, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x60, (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0xc6, (byte) 0x18, (byte) 0x60, (byte) 0x80, (byte) 0x61, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x60, (byte) 0x98, (byte) 0x31, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x8c, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x86, (byte) 0x07, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x60, (byte) 0x00, (byte) 0x06, 
        (byte) 0xc0, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x18, (byte) 0x66, (byte) 0x98, (byte) 0x01, 
        (byte) 0x86, (byte) 0x19, (byte) 0x80, (byte) 0x01, (byte) 0x73, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x19, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x98, (byte) 0x61, 
        (byte) 0x86, (byte) 0x19, (byte) 0x66, (byte) 0x00, (byte) 0x60, 
        (byte) 0x30, (byte) 0x18, (byte) 0x86, (byte) 0x04, (byte) 0x33, 
        (byte) 0xcc, (byte) 0x30, (byte) 0xc3, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x33, 
        (byte) 0x30, (byte) 0x18, (byte) 0x67, (byte) 0x1e, (byte) 0x00, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x83, (byte) 0x01, 
        (byte) 0xcc, (byte) 0xc0, (byte) 0x60, (byte) 0x00, (byte) 0x33, 
        (byte) 0x60, (byte) 0x30, (byte) 0xc3, (byte) 0x0c, (byte) 0x0c, 
        (byte) 0xcc, (byte) 0xc0, (byte) 0x00, (byte) 0x03, (byte) 0x0c, 
        (byte) 0xc0, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x0c, (byte) 0x1b, (byte) 0x66, (byte) 0x0c, (byte) 0x33, 
        (byte) 0xc6, (byte) 0x18, (byte) 0x60, (byte) 0x00, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0xc0, (byte) 0x8c, (byte) 0x61, 
        (byte) 0x06, (byte) 0x18, (byte) 0x66, (byte) 0x18, (byte) 0x33, 
        (byte) 0x06, (byte) 0x30, (byte) 0x63, (byte) 0x0c, (byte) 0x33, 
        (byte) 0x30, (byte) 0x30, (byte) 0x03, (byte) 0x03, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x60, (byte) 0x00, (byte) 0x06, 
        (byte) 0x80, (byte) 0x81, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x18, (byte) 0xe7, (byte) 0x0c, (byte) 0x63, 
        (byte) 0xcc, (byte) 0x31, (byte) 0x86, (byte) 0x01, (byte) 0x6e, 
        (byte) 0x86, (byte) 0xc1, (byte) 0x00, (byte) 0x86, (byte) 0x31, 
        (byte) 0x30, (byte) 0xd8, (byte) 0x66, (byte) 0x18, (byte) 0x33, 
        (byte) 0xce, (byte) 0x10, (byte) 0x67, (byte) 0x80, (byte) 0x61, 
        (byte) 0x30, (byte) 0x18, (byte) 0x87, (byte) 0x07, (byte) 0x33, 
        (byte) 0x86, (byte) 0xe1, (byte) 0x61, (byte) 0x00, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x08, (byte) 0xc2, (byte) 0x13, (byte) 0x00, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x83, (byte) 0x01, 
        (byte) 0x78, (byte) 0xf8, (byte) 0xe7, (byte) 0x1f, (byte) 0x1e, 
        (byte) 0x60, (byte) 0xe0, (byte) 0x81, (byte) 0x07, (byte) 0x0c, 
        (byte) 0x78, (byte) 0x78, (byte) 0x00, (byte) 0x03, (byte) 0x0c, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, 
        (byte) 0xf8, (byte) 0x19, (byte) 0xe6, (byte) 0x07, (byte) 0x1e, 
        (byte) 0x7e, (byte) 0xf8, (byte) 0x67, (byte) 0x00, (byte) 0x1e, 
        (byte) 0x86, (byte) 0xf9, (byte) 0x87, (byte) 0x87, (byte) 0x61, 
        (byte) 0xfe, (byte) 0x19, (byte) 0x66, (byte) 0x18, (byte) 0x1e, 
        (byte) 0x06, (byte) 0xe0, (byte) 0x61, (byte) 0x18, (byte) 0x1e, 
        (byte) 0x30, (byte) 0xe0, (byte) 0x01, (byte) 0x03, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0xe0, (byte) 0x1f, (byte) 0x1e, 
        (byte) 0x80, (byte) 0xe1, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0xf0, (byte) 0x66, (byte) 0x07, (byte) 0x3e, 
        (byte) 0xb8, (byte) 0xe1, (byte) 0x83, (byte) 0x01, (byte) 0x60, 
        (byte) 0x86, (byte) 0xf1, (byte) 0x03, (byte) 0x86, (byte) 0x61, 
        (byte) 0xfc, (byte) 0x18, (byte) 0x66, (byte) 0x18, (byte) 0x1e, 
        (byte) 0x76, (byte) 0xe0, (byte) 0x66, (byte) 0x00, (byte) 0x3f, 
        (byte) 0xe0, (byte) 0xf0, (byte) 0x06, (byte) 0x03, (byte) 0x33, 
        (byte) 0x86, (byte) 0xc1, (byte) 0xe0, (byte) 0x1f, (byte) 0x0c, 
        (byte) 0x30, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x60, (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x33, 
        (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0x06, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x38, 
        (byte) 0x30, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xff, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1e, 
        (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x03, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        (byte) 0x30, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
        };
    
    private static byte[] displayBuf = new byte[(SCREEN_HEIGHT)*(SCREEN_WIDTH+7)/8];
    private static boolean autoRefresh = true;
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

    
    protected final static int LCD_MEM_WIDTH = 60; // width of HW Buffer in bytes
    protected final static int LCD_BUFFER_LENGTH = LCD_MEM_WIDTH*SCREEN_HEIGHT;
    protected NativeDevice dev = new NativeDevice("/dev/fb0");
    protected Pointer lcd = dev.mmap(LCD_BUFFER_LENGTH);
    protected final static byte[] convert = new byte[] {
            (byte)0x00, // 000 00000000
            (byte)0xE0, // 001 11100000
            (byte)0x1C, // 010 00011100
            (byte)0xFC, // 011 11111100
            (byte)0x03, // 100 00000011
            (byte)0xE3, // 101 11100011
            (byte)0x1F, // 110 00011111
            (byte)0xFF  // 111 11111111
    };
    protected byte [] hwBuffer = new byte[LCD_BUFFER_LENGTH];
    protected static LCD singleton = new LCD();

    private LCD() {
        this.setDaemon(true);
        this.start();
    }

    /**
     * Standard two input BitBlt function with the LCD display as the
     * destination. Supports standard raster ops and
     * overlapping images. Images are held in native leJOS/Lego format.
     * @param src byte array containing the source image
     * @param sw Width of the source image
     * @param sh Height of the source image
     * @param sx X position to start the copy from
     * @param sy Y Position to start the copy from
     * @param dx X destination
     * @param dy Y destination
     * @param w width of the area to copy
     * @param h height of the area to copy
     * @param rop raster operation.
     */
    public static void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx, int dy, int w, int h, int rop)
    {
        bitBlt(src, sw, sh, sx, sy, displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, dx, dy, w, h, rop);
    }

    /**
     * Draw a single char on the LCD at specified x,y co-ordinate.
     * @param c Character to display
     * @param x X location
     * @param y Y location
     */
    public static void drawChar(char c, int x, int y)
    {
        bitBlt(font, FONT_WIDTH * NOOF_CHARS, FONT_HEIGHT, FONT_WIDTH * (c-32), 0, x * CELL_WIDTH, y * CELL_HEIGHT, FONT_WIDTH, FONT_HEIGHT, ROP_COPY);
    }

    public static void clearDisplay()
    {
        clear();
    }

    /**
     * Display an optionally inverted string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     * @param inverted if true the string is displayed inverted.
     */
    public static void drawString(String str, int x, int y, boolean inverted)
    {
        if (inverted)
        {
            char[] strData = str.toCharArray();
            // Draw the background rect
            bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * CELL_WIDTH, y * CELL_HEIGHT, strData.length * CELL_WIDTH, CELL_HEIGHT, ROP_SET);
            // and the characters
            for (int i = 0; (i < strData.length); i++)
                bitBlt(font, FONT_WIDTH * NOOF_CHARS, FONT_HEIGHT, FONT_WIDTH * (strData[i]-32), 0, (x + i) * CELL_WIDTH, y * CELL_HEIGHT, FONT_WIDTH, FONT_HEIGHT, ROP_COPYINVERTED);
        } else
            drawString(str, x, y);
    }

    /**
     * Display a string on the LCD at specified x,y co-ordinate.
     *
     * @param str The string to be displayed
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public static void drawString(String str, int x, int y)
    {
        char[] strData = str.toCharArray();
        // Draw the background rect
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * CELL_WIDTH, y * CELL_HEIGHT, strData.length * CELL_WIDTH, CELL_HEIGHT, ROP_CLEAR);
        // and the characters
        for (int i = 0; (i < strData.length); i++)
            bitBlt(font, FONT_WIDTH * NOOF_CHARS, FONT_HEIGHT, FONT_WIDTH * (strData[i]-32), 0, (x + i) * CELL_WIDTH, y * CELL_HEIGHT, FONT_WIDTH, FONT_HEIGHT, ROP_COPY);
    }

    /**
     * Display an int on the LCD at specified x,y co-ordinate.
     *
     * @param i The value to display.
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public static void drawInt(int i, int x, int y)
    {
        drawString(Integer.toString(i), x, y);
    }

    /**
     * Display an in on the LCD at x,y with leading spaces to occupy at least the number
     * of characters specified by the places parameter.
     *
     * @param i The value to display
     * @param places number of places to use to display the value
     * @param x The x character co-ordinate to display at.
     * @param y The y character co-ordinate to display at.
     */
    public static void drawInt(int i, int places, int x, int y)
    {
        drawString(String.format("%"+places+"d", i), x, y);
    }

    /**
     * Start the process of updating the display. This will always return
     * immediately after starting the refresh process.
     */
    public static void asyncRefresh()
    {
        singleton.update(displayBuf);
    }

    /**
     * Obtain the system time when the current display refresh operation will
     * be complete. Not that this may be in the past.
     * @return the system time in ms when the refresh will be complete.
     */
    public static long getRefreshCompleteTime()
    {
        return System.currentTimeMillis();
    }

    /**
     * Wait for the current refresh cycle to complete.
     */
    public static void asyncRefreshWait()
    {
        long waitTime = getRefreshCompleteTime() -  System.currentTimeMillis();
        if (waitTime > 0)
            Delay.msDelay(waitTime);
    }

    /**
     * Refresh the display. If auto refresh is off, this method will wait until
     * the display refresh has completed. If auto refresh is on it will return
     * immediately.
     */
    public static void refresh()
    {
        asyncRefresh();
        if (!autoRefresh)
            asyncRefreshWait();
    }

    /**
     * Clear the display.
     */
    public static void clear()
    {
        bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, ROP_CLEAR);
    }
    /**
     * Provide access to the LCD display frame buffer. Allows both the firmware
     * and Java to make changes.
     * @return byte array that is the frame buffer.
     */
    public static byte[] getDisplay()
    {
        return displayBuf;
    }

    /**
     * Provide access to the LCD system font. Allows both the firmware
     * and Java to share the same font bitmaps.
     * @return byte array that is the frame buffer.
     */
    public static byte[] getSystemFont()
    {
        return font;
    }

    /**
     * Set the period used to perform automatic refreshing of the display.
     * A period of 0 disables the refresh.
     * @param period time in ms
     * @return the previous refresh period.
     */
    public static int setAutoRefreshPeriod(int period)
    {
        return 0;
    }

    /**
     * Turn on/off the automatic refresh of the LCD display. At system startup
     * auto refresh is on.
     * @param on true to enable, false to disable
     */
    public static void setAutoRefresh(boolean on)
    {
        setAutoRefreshPeriod((on ? DEFAULT_REFRESH_PERIOD : 0));
        autoRefresh = on;
    }
    
    /**
     * Method to set a pixel on the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param color the pixel color (0 = white, 1 = black)
     */
    public static void setPixel(int x, int y, int color)
    {
        bitBlt(displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, x, y, 1, 1,(color == 1 ? ROP_SET : ROP_CLEAR));
    }
    
    /**
     * Method to get a pixel from the screen.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the pixel color (0 = white, 1 = black)
     */
    public static int getPixel(int x, int y) {
        if (x < 0 || x >= SCREEN_WIDTH || y < 0 || y >= SCREEN_HEIGHT) return 0; 
        int bit = (y & 0x7);
        int index = (y/8)*SCREEN_WIDTH + x;
        return ((displayBuf[index] >> bit) & 1);
    }

    /**
     * Standard two input BitBlt function. Supports standard raster ops and
     * overlapping images. Images are held in native leJOS/Lego format.
     * @param src byte array containing the source image
     * @param sw Width of the source image
     * @param sh Height of the source image
     * @param sx X position to start the copy from
     * @param sy Y Position to start the copy from
     * @param dst byte array containing the destination image
     * @param dw Width of the destination image
     * @param dh Height of the destination image
     * @param dx X destination
     * @param dy Y destination
     * @param w width of the area to copy
     * @param h height of the area to copy
     * @param rop raster operation.
     */
    public static void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte dst[], int dw, int dh, int dx, int dy, int w, int h, int rop)
    {
        /* This is a partial implementation of the BitBlt algorithm. It provides a
         * complete set of raster operations and handles partial and fully aligned
         * images correctly. Overlapping source and destination images is also 
         * supported. It does not performing mirroring. The code was converted
         * from an initial Java implementation and has not been optimized for C.
         * The general mechanism is to perform the block copy with Y as the inner
         * loop (because on the display the bits are packed y-wise into a byte). We
         * perform the various rop cases by reducing the operation to a series of
         * AND and XOR operations. Each step is controlled by a byte in the rop code.
         * This mechanism is based upon that used in the X Windows system server.
         */
        // Clip to source and destination
        int trim;
        if (dx < 0)
        {
          trim = -dx;
          dx = 0;
          sx += trim;
          w -= trim;
        }
        if (dy < 0)
        {
          trim = -dy;
          dy = 0;
          sy += trim;
          h -= trim;
        }
        if (sx < 0 || sy < 0) return;
        if (dx + w > dw) w = dw - dx;
        if (sx + w > sw) w = sw - sx;
        if (w <= 0) return;
        if (dy + h > dh) h = dh - dy;
        if (sy + h > sh) h = sh - sy;
        if (h <= 0) return;
        // Setup initial parameters and check for overlapping copy
        int xinc = 1;
        int yinc = 1;
        byte firstBit = 1;
        if (src == dst)
        {
          // If copy overlaps we use reverse direction
          if (dy > sy)
          {
            sy = sy + h - 1;
            dy = dy + h - 1;
            yinc = -1;
          }
          if (dx > sx)
          {
            firstBit = (byte)0x80;
            xinc = -1;
            sx = sx + w - 1;
            dx = dx + w - 1;
          }
        }
        if (src == null)
            src = dst;
        int swb = (sw+7)/8;
        int dwb = (dw+7)/8;
        int inStart = sy*swb;
        int outStart = dy*dwb;
        byte inStartBit = (byte)(1 << (sx & 0x7));
        byte outStartBit = (byte)(1 << (dx & 0x7));
        dwb *= yinc;
        swb *= yinc;
        // Extract rop sub-fields
        byte ca1 = (byte)(rop >> 24);
        byte cx1 = (byte)(rop >> 16);
        byte ca2 = (byte)(rop >> 8);
        byte cx2 = (byte) rop;
        boolean noDst = (ca1 == 0 && cx1 == 0);
        int ycnt;
        // Check for byte aligned case and optimise for it
        if (w >= 8 && inStartBit == firstBit && outStartBit == firstBit)
        {
          int ix = sx/8;
          int ox = dx/8;
          int byteCnt = w/8;
          ycnt = h;
          while (ycnt-- > 0)
          {
            int inIndex = inStart + ix;
            int outIndex = outStart + ox;
            int cnt = byteCnt;
            while(cnt-- > 0)
            {
              if (noDst)
                dst[outIndex] = (byte)((src[inIndex] & ca2)^cx2);            
              else
              {
                byte inVal = src[inIndex];
                dst[outIndex] = (byte)((dst[outIndex] & ((inVal & ca1)^cx1)) ^ ((inVal & ca2)^cx2));
              }
              outIndex += xinc;
              inIndex += xinc;
            }
            ix += swb;
            ox += dwb;
          }
          // Do we have a final non byte multiple to do?
          w &= 0x7;
          if (w == 0) 
          {
              if (dst == displayBuf)
                  singleton.update(displayBuf);
              return;
          }
          //inStart = sy*swb;
          //outStart = dy*dwb;
          sx += byteCnt*8;
          dx += byteCnt*8;
        }
        // General non byte aligned case
        int ix = sx/8;
        int ox = dx/8;
        ycnt = h;
        while(ycnt-- > 0)
        {
          int inIndex = inStart + ix;
          byte inBit = inStartBit;
          byte inVal = src[inIndex];
          byte inAnd = (byte)((inVal & ca1)^cx1);
          byte inXor = (byte)((inVal & ca2)^cx2);
          int outIndex = outStart + ox;
          byte outBit = outStartBit;
          byte outPixels = dst[outIndex];
          int cnt = w;
          while(true)
          {
            if (noDst)
            {
              if ((inXor & inBit) != 0)
                outPixels |= outBit;
              else
                outPixels &= ~outBit;
            }
            else
            {
              byte resBit = (byte)((outPixels & ((inAnd & inBit) != 0 ? outBit : 0))^((inXor & inBit) != 0 ? outBit : 0));
              outPixels = (byte)((outPixels & ~outBit) | resBit);
            }
            if (--cnt <= 0) break;
            if (xinc > 0)
            {
              inBit <<= 1;
              outBit <<= 1;
            }
            else
            {
              inBit >>= 1;
              outBit >>= 1;
            }
            if (inBit == 0)
            {
              inBit = firstBit;
              inIndex += xinc;
              inVal = src[inIndex];
              inAnd = (byte)((inVal & ca1)^cx1);
              inXor = (byte)((inVal & ca2)^cx2);
            }
            if (outBit == 0)
            {
              dst[outIndex] = outPixels;
              outBit = firstBit;
              outIndex += xinc;
              outPixels = dst[outIndex];
            }
          }
          dst[outIndex] = outPixels;
          inStart += swb;
          outStart += dwb;
        }
      }        
    /**
     * Scrolls the screen up one text line
     *
     */
    public static void scroll()
    {
        LCD.bitBlt(displayBuf, SCREEN_WIDTH, SCREEN_HEIGHT, 0, CELL_HEIGHT,
                0, 0, SCREEN_WIDTH, SCREEN_HEIGHT - CELL_HEIGHT, ROP_COPY);
        LCD.bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, SCREEN_HEIGHT - CELL_HEIGHT,
                SCREEN_WIDTH, CELL_HEIGHT, ROP_CLEAR);
    }
    
    /**
     * Clear a contiguous set of characters
     * @param x the x character coordinate
     * @param y the y character coordinate
     * @param n the number of characters
     */
    public static void clear(int x, int y, int n) {
        LCD.bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, x * CELL_WIDTH, y * CELL_HEIGHT,
                n * CELL_WIDTH, CELL_HEIGHT, ROP_CLEAR);
    }
    
    /**
     * Clear an LCD display row
     * @param y the row to clear
     */
    public static void clear(int y) {
        LCD.bitBlt(null, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 0, y * CELL_HEIGHT,
                SCREEN_WIDTH, CELL_HEIGHT, ROP_CLEAR);      
    }

    /**
     * Set the LCD contrast.
     * @param contrast 0 blank 0x60 full on
     */
    public static void setContrast(int contrast)
    {
        
    }

    

    /**
     * Update the hardware display contents from the internal display bitmap. We 
     * need to convert the data from the simple packed format used by the internal
     * display to that used by the actual LCD controller. See the datasheet for the
     * ST7586S LCD controller for details.
     * @param buffer internal format buffer to display
     */
    protected synchronized void update(byte [] buffer)
    {
        int inOffset = 0;
        int outOffset = 0;
        for(int row = 0; row < SCREEN_HEIGHT; row++)
        {
            int pixels;
            for(int i = 0; i < 7; i++)
            {
                pixels = buffer[inOffset++] & 0xff;
                pixels |= (buffer[inOffset++] & 0xff) << 8;
                pixels |= (buffer[inOffset++] & 0xff) << 16;
                
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
                pixels >>= 3;
                hwBuffer[outOffset++] = convert[pixels & 0x7];
            }
            pixels = buffer[inOffset++] & 0xff;
            pixels |= (buffer[inOffset++] & 0xff) << 8;
            hwBuffer[outOffset++] = convert[pixels & 0x7];
            pixels >>= 3;
            hwBuffer[outOffset++] = convert[pixels & 0x7];
            pixels >>= 3;
            hwBuffer[outOffset++] = convert[pixels & 0x7];
            pixels >>= 3;
            hwBuffer[outOffset++] = convert[pixels & 0x7];
        }
        //lcd.put(hwBuffer);
        lcd.write(0, hwBuffer, 0, hwBuffer.length);
    }

    /**
     * Background thread which provides automatic screen updates
     */
    public void run()
    {
        for(;;)
        {
            Delay.msDelay(DEFAULT_REFRESH_PERIOD);
            if (autoRefresh)
                update(displayBuf);
        }
    }
}

