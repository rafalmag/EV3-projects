package lejos.ev3.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class LCDDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final int LCD_WIDTH = 178;
    private static final int LCD_HEIGHT = 128;
    
    private BufferedImage lcd = new BufferedImage(LCD_WIDTH, LCD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D lcdGC = lcd.createGraphics();

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paint(g);
        int width = getWidth();
        int height = getHeight();
        int imgWidth = lcd.getWidth();
        int imgHeight = lcd.getHeight();
        // Draw a scaled version of the display, keep the aspect ratio and
        // centre it.
        if (width < (height*imgWidth)/imgHeight)
        {
            imgHeight = (width*imgHeight)/imgWidth;
            imgWidth = width;
        }
        else
        {
            imgWidth = (height*imgWidth)/imgHeight;
            imgHeight = height;
        }
        g2d.drawImage(lcd, (width-imgWidth)/2, (height-imgHeight)/2, imgWidth, imgHeight, null);

    }
    
    public void clear()
    {
        lcdGC.setColor(new Color(155, 205, 155, 255));
        lcdGC.fillRect(0, 0, lcd.getWidth(), lcd.getHeight());
    }

    public void update(byte [] buffer)
    {
        lcdGC.setColor(new Color(155, 205, 155, 255));
        lcdGC.fillRect(0, 0, lcd.getWidth(), lcd.getHeight());
        lcdGC.setColor(new Color(0, 0, 0, 255));
        for(int y = 0;y<LCD_HEIGHT;y++) {
        	for(int x = 0; x<LCD_WIDTH;x++) {
        		int i = (y * (LCD_WIDTH/8 + 1) * 8) + x;
                int bit = (i & 0x7);
                int index = i / 8;
                int val =  ((buffer[index] >> bit) & 1);
        		if (val == 1) lcdGC.fillRect(x,y, 1, 1);
        	}
        }
        this.repaint();
    }

}

