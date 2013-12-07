
package lejos.hardware.ev3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.Battery;
import lejos.hardware.Bluetooth;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.Wifi;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.internal.ev3.EV3Audio;
import lejos.internal.ev3.EV3DeviceManager;
import lejos.internal.ev3.EV3GraphicsLCD;
import lejos.internal.ev3.EV3Port;
import lejos.internal.ev3.EV3Battery;
import lejos.internal.ev3.EV3TextLCD;

/**
 * This class represents the local instance of an EV3 device. It can be used to
 * obtain access to the various system resources (Sensors, Motors etc.).
 * @author andy
 *
 */
public class LocalEV3 implements EV3
{
    static
    {
        // Check that we have EV3 hardware available
        EV3DeviceManager.getLocalDeviceManager();
    }
    
    public static final LocalEV3 ev3 = new LocalEV3();
    public final Battery battery = new EV3Battery();
    protected final Audio audio = EV3Audio.getAudio();
    protected ArrayList<EV3Port> ports = new ArrayList<EV3Port>();
    protected TextLCD textLCD;
    protected GraphicsLCD graphicsLCD;
    
    private LocalEV3()
    {
        // Create the port objects
        ports.add(new EV3Port("S1", EV3Port.SENSOR_PORT, 0));
        ports.add(new EV3Port("S2", EV3Port.SENSOR_PORT, 1));
        ports.add(new EV3Port("S3", EV3Port.SENSOR_PORT, 2));
        ports.add(new EV3Port("S4", EV3Port.SENSOR_PORT, 3));
        ports.add(new EV3Port("A", EV3Port.MOTOR_PORT, 0));
        ports.add(new EV3Port("B", EV3Port.MOTOR_PORT, 1));
        ports.add(new EV3Port("C", EV3Port.MOTOR_PORT, 2));
        ports.add(new EV3Port("D", EV3Port.MOTOR_PORT, 3));
    }
    
    public static EV3 get()
    {
        return ev3;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public Port getPort(String portName)
    {
        for(EV3Port p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
    }
    
    
    /** {@inheritDoc}
     */    
    @Override
    public Battery getBattery()
    {
        return battery;
    }

	@Override
	public TextLCD getTextLCD() {
		if (textLCD == null) textLCD = new EV3TextLCD();
		return textLCD;
	}

	@Override
	public GraphicsLCD getGraphicsLCD() {
		
		if (graphicsLCD == null) graphicsLCD = new EV3GraphicsLCD(); 
		return graphicsLCD;
	}

	@Override
	public TextLCD getTextLCD(Font f) {
		return new EV3TextLCD(f);
	}
	
    /** {@inheritDoc}
     */    
    @Override
    public Audio getAudio()
    {
        return audio;
    }

	@Override
	public boolean isLocal() {
		return true;
	}

	@Override
	public String getType() {
		return "EV3";
	}

	@Override
	public String getName() {
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("/etc/hostname"));
			String name = in.readLine().trim();
			in.close();
			return name;
		} catch (IOException e) {
			return "Not known";
		}
	}

	@Override
	public LocalBTDevice getBluetoothDevice() {
		return Bluetooth.getLocalDevice();
	}

	@Override
	public LocalWifiDevice getWifiDevice() {
		return Wifi.getLocalDevice("wlan0");
	}
}
