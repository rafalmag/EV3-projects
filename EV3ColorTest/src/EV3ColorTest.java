import java.io.File;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import lejos.nxt.LCD;
import lejos.nxt.LocalUARTPort;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UARTPort;
import lejos.util.Delay;


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

        UARTPort u = new LocalUARTPort();
        if (!u.open(3))
        {
            System.out.println("Open failed");
            return;
        }
        u.setMode(2);
        Graphics g = new Graphics();
        final int SW = LCD.SCREEN_WIDTH;
        final int SH = LCD.SCREEN_HEIGHT;
        byte oldColor = 0;
        String []colorNames = {"None", "Black", "Blue", "Green", "Yellow", "Red", "White"};
        File colorSounds[] = new File[colorNames.length];
        for(int i = 1; i < colorSounds.length; i++)
            colorSounds[i] = new File(colorNames[i].toLowerCase()+".wav");
        g.clear();
        g.setFont(Font.getLargeFont());
        while (true)
        {
            byte newColor = u.getByte();
            if (newColor != 0 && newColor == oldColor)
            {
                g.clear();
                g.drawString(colorNames[newColor], SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
                System.out.println("play returns " + Sound.playSample(colorSounds[newColor], 100));
                while(u.getByte() == newColor)
                    Delay.msDelay(500);
                g.clear();
            }
            oldColor = newColor;
            Delay.msDelay(500);
        }
    }

}
