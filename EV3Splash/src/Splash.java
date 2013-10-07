import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.hardware.Button;
import lejos.hardware.LCD;


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
    
    public static List<String> getIPAddresses()
    {
        List<String> result = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces;
        try
        {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1)
        {
            e1.printStackTrace();
            return null;
        }
        while (interfaces.hasMoreElements()){
            NetworkInterface current = interfaces.nextElement();
            try
            {
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
            } catch (SocketException e)
            {
                e.printStackTrace();
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()){
                InetAddress current_addr = addresses.nextElement();
                if (current_addr.isLoopbackAddress()) continue;
                result.add(current_addr.getHostAddress());
            }
        }
        return result;
    }    

    public void displaySplash(String ver)
    {
        g.clear();
        //g.setFont(Font.getDefaultFont());
        g.drawRegion(logo, 0, 0, logo.getWidth(), logo.getHeight(), 0, 0, 0, 0);
        g.setFont(Font.getLargeFont());
        g.drawString("leJOS", (SW-logo.getWidth())/ 2 + logo.getWidth(), 0, Graphics.TOP|Graphics.HCENTER);
        g.drawString("EV3", (SW-logo.getWidth())/ 2 + logo.getWidth(), Font.getLargeFont().getHeight(), Graphics.TOP|Graphics.HCENTER);
        g.setFont(Font.getDefaultFont());
        g.drawString(ver, SW/2, logo.getHeight()+ 5, Graphics.TOP|Graphics.HCENTER);
        int cnt = 1;
        for(String s:getIPAddresses())
        {
            g.drawString(s, SW/2, logo.getHeight()+ 5 + cnt*Font.getDefaultFont().getHeight(), Graphics.TOP|Graphics.HCENTER);
            cnt++;
        }
        LCD.refresh();
    }
    
    public static void main(String[] args)
    {
        Splash s = new Splash();
        s.displaySplash(args[0]);
    }

}
