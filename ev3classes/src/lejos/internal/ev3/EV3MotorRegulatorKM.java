package lejos.internal.ev3;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
 * Implementation of a PID based motor regulator that uses a kernel module
 * for the core regulation operations. This mechanism is accessed via the
 * EV3MotorPort class.
 **/
public class EV3MotorRegulatorKM implements MotorRegulator
{

    protected EV3MotorPort tachoPort;
    protected int zeroTachoCnt;
    protected int limitAngle;


    public EV3MotorRegulatorKM(TachoMotorPort p)
    {
        tachoPort = (EV3MotorPort)p;
        tachoPort.setPWMMode(TachoMotorPort.PWM_BRAKE);
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
        return tachoPort.getCurrentVelocity();
    }
    
    public void setStallThreshold(int error, int time)
    {
        //this.stallLimit = error;
        //this.stallTime = time/Controller.UPDATE_PERIOD;
    }

    /**
     * return the regulations models current position. Ensure that the motor is active
     * if needed.
     * @return the models current position
     */
    synchronized public float getPosition()
    {
        //return curCnt;
       return tachoPort.getPosition() - zeroTachoCnt; 
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


    @Override
    public void setControlParamaters(int typ, float moveP, float moveI,
            float moveD, float holdP, float holdI, float holdD, int offset)
    {
        tachoPort.setControlParams(typ, moveP, moveI, moveD, holdP, holdI, holdD, offset, 0);
    }


    @Override
    public void waitComplete()
    {
        tachoPort.waitComplete();
    }


    @Override
    public void addListener(RegulatedMotor motor, RegulatedMotorListener listener)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public RegulatedMotorListener removeListener()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int getLimitAngle()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public boolean isStalled()
    {
        // TODO Auto-generated method stub
        return tachoPort.isStalled();
    }

}