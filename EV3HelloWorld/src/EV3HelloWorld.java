import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.Sound;
import lejos.utility.Delay;
public class EV3HelloWorld
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("Running...");
        final int SW = LCD.SCREEN_WIDTH;
        final int SH = LCD.SCREEN_HEIGHT;
        Button.LEDPattern(4);
        Sound.beepSequenceUp();
        Graphics g = new Graphics();
        g.setFont(Font.getLargeFont());
        g.drawString("leJOS/EV3", SW/2, SH/2, Graphics.BASELINE|Graphics.HCENTER);
        Button.LEDPattern(3);
        Delay.msDelay(4000);
        Button.LEDPattern(5);
        g.clear();
        LCD.refresh();
        Sound.beepSequence();
        Delay.msDelay(500);
        Button.LEDPattern(0);

        
    }

}
