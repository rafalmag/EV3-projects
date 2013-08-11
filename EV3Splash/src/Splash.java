import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.Button;
import lejos.nxt.LCD;


public class Splash
{
    Graphics g = new Graphics();
    final int SW = LCD.SCREEN_WIDTH;
    final int SH = LCD.SCREEN_HEIGHT;
    Image logo = new Image(52, 64, new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, 
            (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x33, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x30, 
            (byte) 0x33, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x30, (byte) 0x33, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0xcc, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xcc, (byte) 0xcc, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0x0f, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x0f, (byte) 0xff, 
            (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0xff, 
            (byte) 0x0f, (byte) 0xff, (byte) 0x3f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xc0, (byte) 0xff, (byte) 0x0f, (byte) 0xff, (byte) 0xff, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0x0f, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xf0, 
            (byte) 0xff, (byte) 0x0f, (byte) 0xfc, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x03, (byte) 0xfc, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x03, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xf0, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0x0f, (byte) 0x00, (byte) 0x00, 
            (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, 
            (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0x00, (byte) 0x00, });
    public static String getIPAddress()
    {
        // TODO Auto-generated method stub
        Enumeration<NetworkInterface> interfaces;
        try
        {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "";
        }
        while (interfaces.hasMoreElements()){
            NetworkInterface current = interfaces.nextElement();
            try
            {
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
            } catch (SocketException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()){
                InetAddress current_addr = addresses.nextElement();
                if (current_addr.isLoopbackAddress()) continue;
                return current_addr.getHostAddress();
            }
        }
        return "";
    }    

    public void displaySplash()
    {
        g.clear();
        g.setFont(Font.getLargeFont());
        //g.setFont(Font.getDefaultFont());
        g.drawRegion(logo, 0, 0, logo.getWidth(), logo.getHeight(), 0, SW / 2, SH / 4 , Graphics.HCENTER | Graphics.VCENTER);
        g.drawString("leJOS/EV3", SW/2, 3*SH/4, Graphics.BASELINE|Graphics.HCENTER);
        g.setFont(Font.getDefaultFont());
        g.drawString(getIPAddress(), SW/2, (3*SH/4)+3*Font.getDefaultFont().getHeight()/2, Graphics.BASELINE|Graphics.HCENTER);
        LCD.refresh();
    }
    
    public static void main(String[] args)
    {
        Splash s = new Splash();
        s.displaySplash();
    }

}
