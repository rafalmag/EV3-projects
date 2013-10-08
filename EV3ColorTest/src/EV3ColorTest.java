import java.io.File;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import lejos.hardware.LCD;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.internal.ev3.EV3UARTPort;
import lejos.utility.Delay;


public class EV3ColorTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        System.out.println("Running...");

        EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S4);
        Graphics g = new Graphics();
        final int SW = LCD.SCREEN_WIDTH;
        final int SH = LCD.SCREEN_HEIGHT;
        int oldColor = 0;
        String []colorNames = {"None", "Red", "Green", "Blue", "Yellow", "Magenta",
                "Orange", "White", "Black", "Pink", "Grey", "Light Grey", "Dark Grey",
                "Cyan", "Brown"};
        File colorSounds[] = new File[colorNames.length];
        for(int i = 1; i < colorSounds.length; i++)
            colorSounds[i] = new File(colorNames[i].toLowerCase()+".wav");
        g.clear();
        g.setFont(Font.getLargeFont());
        while (true)
        {
            int newColor = cs.getColorID();
            if (newColor != -1 && newColor == oldColor)
            {
                g.clear();
                g.drawString(colorNames[newColor+1], SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
                System.out.println("play returns " + Sound.playSample(colorSounds[newColor+1], 100));
                while(cs.getColorID() == newColor)
                    Delay.msDelay(500);
                g.clear();
            }
            oldColor = newColor;
            Delay.msDelay(500);
        }
    }

}
