import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;


public class Splash
{
    GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
    final int SW = g.getWidth();
    final int SH = g.getHeight();
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
        g.drawString("leJOS", (SW-logo.getWidth())/ 2 + logo.getWidth(), 0, GraphicsLCD.TOP|GraphicsLCD.HCENTER);
        g.drawString("EV3", (SW-logo.getWidth())/ 2 + logo.getWidth(), Font.getLargeFont().getHeight(), GraphicsLCD.TOP|GraphicsLCD.HCENTER);
        g.setFont(Font.getDefaultFont());
        g.drawString(ver, SW/2, logo.getHeight()+ 15, GraphicsLCD.TOP|GraphicsLCD.HCENTER);
        int cnt = 1;
        /*
        for(String s:getIPAddresses())
        {
            g.drawString(s, SW/2, logo.getHeight()+ 5 + cnt*Font.getDefaultFont().getHeight(), Graphics.TOP|Graphics.HCENTER);
            cnt++;
        }*/
        g.refresh();
    }
    
    public static void main(String[] args)
    {
        Splash s = new Splash();
        s.displaySplash(args[0]+" "+args[1]);
    }

}
