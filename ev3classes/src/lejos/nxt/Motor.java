package lejos.nxt;

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
    public static final NXTRegulatedMotor A = LocalEV3.A();
    /**
     * Motor B.
     */
    public static final NXTRegulatedMotor B = LocalEV3.B();;
    /**
     * Motor C.
     */
    public static final NXTRegulatedMotor C = LocalEV3.C();;
    
    /**
     * Motor D.
     */
    public static final NXTRegulatedMotor D = LocalEV3.D();;
    
    private Motor() {
    	// Motor class cannot be instantiated
    }

    /**
     * Return the Motor with the given Id.
     * @param id the Id, between 0 and {@link MotorPort#NUMBER_OF_PORTS}-1.
     * @return the MotorPort object
     */
    public static NXTRegulatedMotor getInstance(int id)
    {
        return LocalEV3.ev3.getMotor(id);
    }

}
