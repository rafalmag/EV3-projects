package lejos.hardware.ev3;

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
    
    //TODO: Extend to include motor ports and perhaps motors, also buttons, sound, LCD etc.
    
    public TextLCD getTextLCD();
    
    public TextLCD getTextLCD(Font f);
    
    public GraphicsLCD getGraphicsLCD();

}
