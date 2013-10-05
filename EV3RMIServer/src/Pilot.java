import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;

public interface Pilot extends Remote {
	
	public void forward() throws RemoteException;
	
	public void stop() throws RemoteException;
	
	public void arcForward(double radius) throws RemoteException;
	
	public void arcBackward(double radius) throws RemoteException;
	
	public void arc(double radius, double angle) throws RemoteException;
	
	public void arc(double radius, double angle, boolean immediateReturn) throws RemoteException;
	
	public void travelArc(double radius, double distance) throws RemoteException;
	
	public void travelArc(double radius, double distance,
			boolean immediateReturn)  throws RemoteException;
	
	public void backward()  throws RemoteException;
	
	public boolean isMoving() throws RemoteException;
	
	public void travel(double distance) throws RemoteException;
	
	public void travel(double distance, boolean immediateReturn) throws RemoteException ;
	
	public void setTravelSpeed(double speed) throws RemoteException;
	
	public double getTravelSpeed() throws RemoteException;
	
	public double getMaxTravelSpeed() throws RemoteException ;
	
	public Move getMovement() throws RemoteException;
	
	public void addMoveListener(MoveListener listener) throws RemoteException;
	
	public void rotate(double angle) throws RemoteException;
	
	public void rotate(double angle, boolean immediateReturn) throws RemoteException;
	
	public void setRotateSpeed(double speed) throws RemoteException;
	
	public double getRotateSpeed() throws RemoteException;
	
	public double getRotateMaxSpeed() throws RemoteException;

}
