import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;

public class RemotePilot extends UnicastRemoteObject implements Pilot  {
	
	private static final long serialVersionUID = 2469646614339905936L;
	
	private ArcRotateMoveController pilot;
	
    public RemotePilot(ArcRotateMoveController pilot) throws RemoteException {
        super(0);
        this.pilot = pilot;
    }

	@Override
	public void forward() throws RemoteException {
		System.out.println("Executing forward");
		pilot.forward();
	}

	@Override
	public void stop() throws RemoteException {
		System.out.println("Executing stop");
		pilot.stop();	
	}

	@Override
	public void arcForward(double radius) throws RemoteException {
		pilot.arcForward(radius);
	}

	@Override
	public void arcBackward(double radius) throws RemoteException {
		pilot.arcBackward(radius);
	}


	@Override
	public void arc(double radius, double angle, boolean immediateReturn) throws RemoteException {
		pilot.arc(radius, angle, immediateReturn);	
	}

	@Override
	public void travelArc(double radius, double distance) throws RemoteException  {
		pilot.travelArc(radius, distance);
	}

	@Override
	public void travelArc(double radius, double distance,
			boolean immediateReturn)  throws RemoteException {
		pilot.travel(distance, immediateReturn);
	}

	@Override
	public void backward()  throws RemoteException {
		pilot.backward();	
	}

	@Override
	public boolean isMoving() throws RemoteException  {
		return pilot.isMoving();
	}

	@Override
	public void travel(double distance) throws RemoteException {
		pilot.travel(distance);
	}

	@Override
	public void travel(double distance, boolean immediateReturn) throws RemoteException {
		pilot.travel(distance, immediateReturn);
	}

	@Override
	public void setTravelSpeed(double speed) throws RemoteException {
		pilot.setTravelSpeed(speed);		
	}

	@Override
	public double getTravelSpeed() throws RemoteException {
		return pilot.getTravelSpeed();
	}

	@Override
	public double getMaxTravelSpeed() throws RemoteException {
		return pilot.getMaxTravelSpeed();
	}

	@Override
	public Move getMovement() throws RemoteException {
		return pilot.getMovement();
	}

	@Override
	public void rotate(double angle) throws RemoteException {
		pilot.rotate(angle);
	}

	@Override
	public void rotate(double angle, boolean immediateReturn) throws RemoteException {
		pilot.rotate(angle, immediateReturn);
	}

	@Override
	public void setRotateSpeed(double speed) throws RemoteException {
		pilot.setRotateSpeed(speed);
	}

	@Override
	public double getRotateSpeed() throws RemoteException {
		return pilot.getRotateSpeed();
	}

	@Override
	public double getRotateMaxSpeed() throws RemoteException {
		return pilot.getRotateMaxSpeed();
	}

	@Override
	public void arc(double radius, double angle) throws RemoteException {
		pilot.arc(radius, angle);
	}

	@Override
	public void addMoveListener(MoveListener listener) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
