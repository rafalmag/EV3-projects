package lejos.hardware.motor;

import lejos.hardware.ev3.LocalEV3;

/**
 * Motor class contains 3 instances of regulated motors.
 * <p>
 * Example:<p>
 * <code><pre>
 *   Motor.A.setSpeed(720);// 2 RPM
 *   Motor.C.setSpeed(720);
 *   Motor.A.forward();
 *   Motor.C.forward();
 *   Thread.sleep (1000);
 *   Motor.A.stop();
 *   Motor.C.stop();
 *   Motor.A.rotateTo( 360);
 *   Motor.A.rotate(-720,true);
 *   while(Motor.A.isMoving() :Thread.yield();
 *   int angle = Motor.A.getTachoCount(); // should be -360
 *   LCD.drawInt(angle,0,0);
 * </pre></code>
 * @author Roger Glassey/Andy Shaw
 */
public class Motor
{
    /**
     * Motor A.
     */
    public static final NXTRegulatedMotor A = new NXTRegulatedMotor(LocalEV3.ev3.getPort("A"));
    /**
     * Motor B.
     */
    public static final NXTRegulatedMotor B = new NXTRegulatedMotor(LocalEV3.ev3.getPort("B"));
    /**
     * Motor C.
     */
    public static final NXTRegulatedMotor C = new NXTRegulatedMotor(LocalEV3.ev3.getPort("C"));
    
    /**
     * Motor D.
     */
    public static final NXTRegulatedMotor D = new NXTRegulatedMotor(LocalEV3.ev3.getPort("D"));
    
    private Motor() {
    	// Motor class cannot be instantiated
    }


}
