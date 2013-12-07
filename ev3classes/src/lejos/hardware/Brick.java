package lejos.hardware;

import lejos.hardware.Audio;
import lejos.hardware.Battery;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;

public interface Brick
{
    /**
     * Return a port object for the request port name. This allows access to the
     * hardware associated with the specified port.
     * @param portName The name of port
     * @return the request port
     */
    public Port getPort(String portName);
    
    /**
     * return a battery object which can be used to obtain battery voltage etc.
     * @return A battery object
     */
    public Battery getBattery();

    /**
     * return a Audio object which can be used to access the device's audio playback
     * @return A Audio device
     */
    public Audio getAudio();

    /**
     * Get text access to the LCD using the default font
     * @return the text LCD 
     */
    public TextLCD getTextLCD();
    
    /**
     * Get text access to the LCD using a specified font
     * @param f the font
     * @return the text LCD
     */
    public TextLCD getTextLCD(Font f);
    
    /**
     * Get graphics access to the LCD
     * @return the graphics LCD
     */
    public GraphicsLCD getGraphicsLCD();

}
