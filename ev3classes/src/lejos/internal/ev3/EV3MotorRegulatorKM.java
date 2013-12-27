package lejos.internal.ev3;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;
import lejos.utility.Delay;

/**
 * Inner class to regulate velocity; also stop motor at desired rotation angle.
 * This class uses a very simple movement model based on simple linear
 * acceleration. This model is used to generate ideal target positions which
 * are then used to generate error terms between the actual and target position
 * this error term is then used to drive a PID style motor controller to
 * regulate the power supplied to the motor.
 *
 * If new command are issued while a move is in progress, the new command
 * is blended with the current one to provide smooth movement.
 *
 * If the requested speed is not possible then the controller will simply
 * drop move cycles until the motor catches up with the ideal position. If
 * too many consecutive dropped moves are required then the motor is viewed
 * to have stalled and the move is terminated.
 *
 * Once the motor stops, the final position is held using the same PID control
 * mechanism (with slightly different parameters), as that used for movement.
 **/
public class EV3MotorRegulatorKM implements MotorRegulator
{
    // PID constants for move and for hold
    // Old values
    //static final float MOVE_P = 4f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 32f;
    // New values
    //static final float MOVE_P = 7f;
    //static final float MOVE_P = 6f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 22f;
    //static final float MOVE_P = 4f;
    //static final float MOVE_I = 0.04f;
    //static final float MOVE_D = 10f;
    static final float MOVE_P = 4f;
    static final float MOVE_I = 0.04f;
    static final float MOVE_D = 10f;
    static final float HOLD_P = 2f;
    static final float HOLD_I = 0.02f;
    static final float HOLD_D = 8f;
    protected EV3MotorPort tachoPort;
    protected int zeroTachoCnt;


    public EV3MotorRegulatorKM(TachoMotorPort p)
    {
        tachoPort = (EV3MotorPort)p;
        tachoPort.setPWMMode(TachoMotorPort.PWM_BRAKE);
        tachoPort.setControlParams(EV3SensorConstants.TYPE_TACHO, MOVE_P, MOVE_I, MOVE_D, HOLD_P, HOLD_I, HOLD_D);
    }
    
    
    public int getTachoCount()
    {
        return tachoPort.getTachoCount() - zeroTachoCnt;
    }
    
    public void resetTachoCount()
    {
        zeroTachoCnt = tachoPort.getTachoCount();
    }

    public boolean isMoving()
    {
        return tachoPort.isMoving();
    }
    
    public float getCurrentVelocity()
    {
        return 0f;
    }
    
    public void setStallThreshold(int error, int time)
    {
        //this.stallLimit = error;
        //this.stallTime = time/Controller.UPDATE_PERIOD;
    }

    /**
     * Reset the tachometer readings
     */
    public synchronized void reset()
    {
        //curCnt = tachoCnt = getTachoCount();
        //baseTime = now = System.currentTimeMillis();
    }


    /**
     * return the regulations models current position. Ensure that the motor is active
     * if needed.
     * @return the models current position
     */
    synchronized public float getPosition()
    {
        //return curCnt;
       return 0F - zeroTachoCnt; 
    }

    /**
     * Initiate a new move and optionally wait for it to complete.
     * If some other move is currently executing then ensure that this move
     * is terminated correctly and then start the new move operation.
     * @param speed
     * @param acceleration
     * @param limit
     * @param hold
     * @param waitComplete
     */
    synchronized public void newMove(float speed, int acceleration, int limit, boolean hold, boolean waitComplete)
    {
        if (Math.abs(limit) != NO_LIMIT)
            limit += zeroTachoCnt;
        tachoPort.newMove(speed, acceleration, limit, hold, waitComplete);
    }

    /**
     * The target speed has been changed. Reflect this change in the
     * regulator.
     * @param newSpeed new target speed.
     */
    public synchronized void adjustSpeed(float newSpeed)
    {
        //if (curTargetVelocity != 0)
        //{
            //startSubMove(newSpeed, curAcc, curLimit, curHold);
        //}
        //if (pending)
            //this.newSpeed = newSpeed;
    }

    /**
     * The target acceleration has been changed. Updated the regulator.
     * @param newAcc
     */
    public synchronized void adjustAcceleration(int newAcc)
    {
        //if (curTargetVelocity != 0)
        //{
            //startSubMove(Math.abs(curTargetVelocity), newAcc, curLimit, curHold);
        //}
        //if (pending)
            //newAcceleration = newAcc;
    }



}