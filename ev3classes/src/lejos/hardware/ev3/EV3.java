package lejos.hardware.ev3;

import lejos.hardware.Audio;
import lejos.hardware.Battery;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;

public interface EV3
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
    
    //TODO: Extend to include motor ports and perhaps motors, also buttons, sound, LCD etc.
    
    public TextLCD getTextLCD();
    
    public TextLCD getTextLCD(Font f);
    
    public GraphicsLCD getGraphicsLCD();

}
