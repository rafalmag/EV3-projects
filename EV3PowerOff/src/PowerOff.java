import lejos.internal.io.NativeDevice;
import lejos.nxt.Button;




/** Simple example of JNA interface mapping and usage. */
public class PowerOff
{

    
    public static void main(String[] args)
    {
        NativeDevice pwr = new NativeDevice("/dev/lms_power");
        // Set to power off
        pwr.ioctl(0, (byte[])null);
        Button.LEDPattern(2);
    }
}