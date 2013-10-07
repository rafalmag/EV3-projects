package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.Gyroscope;
import lejos.util.Delay;

/**
 * Simple driver for the Lego EV3 Gyro sensor.<br>
 * NOTE: This class does not attempt to perform any sort of dynamic drift correction.
 * Any such correction needs to either be external, or at least optional as it is
 * not really possible for this class to know what assumptions can be made about
 * any sort of steady state.
 * 
 * @author andy
 *
 */
public class EV3GyroSensor extends UARTSensor implements Gyroscope
{
    public final static int ANGLE_MODE = 0;
    public final static int RATE_MODE = 1;
    
    protected final static int OFFSET_SAMPLES = 100;
    
    protected float offset = 0.0f;
    
    public EV3GyroSensor(UARTPort p)
    {
        super(p, RATE_MODE);
    }
    
    public EV3GyroSensor(Port p)
    {
        super(p, RATE_MODE);
    }
    
    /** Calculate and return the angular velocity in degrees per second.
     * @return Angular velocity in degrees/second
     */
    public float getAngularVelocity()
    {
        return (float)port.getShort() - offset;
    }

    /** Calculate and set the offset/bias value for use in <code>getAngularVelocity()</code>.
     */
    public void recalibrateOffset()
    {
        offset = 0.0f;
        double gSum;
        float gMin;
        float gMax;
        do {
            gSum = 0.0;
            gMin = Float.MAX_VALUE;
            gMax = -Float.MAX_VALUE;
            for (int i=0; i<OFFSET_SAMPLES; i++) 
            {
                float g = getAngularVelocity();
                if (g > gMax)
                    gMax = g;
                if (g < gMin)
                    gMin = g;

                gSum += g;
                Delay.msDelay(5);
            }
            //System.out.println("Gyro Calibrate min " + gMin + " max " + gMax);
        } while ((gMax - gMin) > 5);   // Reject and sample again if range too large

        //Average the sum of the samples.
        offset = (float)(gSum / OFFSET_SAMPLES); // TODO: Used to have +1, which was mainly for stopping Segway wandering.          
    }
}
