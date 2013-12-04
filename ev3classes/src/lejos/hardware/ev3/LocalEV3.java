package lejos.hardware.ev3;

import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.Battery;
import lejos.hardware.port.Port;
import lejos.internal.ev3.EV3Audio;
import lejos.internal.ev3.EV3DeviceManager;
import lejos.internal.ev3.EV3Port;
import lejos.internal.ev3.EV3Battery;

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
    public final Audio audio = EV3Audio.getAudio();
    protected ArrayList<EV3Port> ports = new ArrayList<EV3Port>();
    
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

    /** {@inheritDoc}
     */    
    @Override
    public Audio getAudio()
    {
        return audio;
    }
}
