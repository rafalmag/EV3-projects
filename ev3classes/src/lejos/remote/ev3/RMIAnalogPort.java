package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIAnalogPort extends Remote {
	
	public int getPin6() throws RemoteException;

	public int getPin1() throws RemoteException;
	
	public void setPinMode(int mode) throws RemoteException;
	
	public void close() throws RemoteException;

    public void getShorts(short[] vals, int offset, int length) throws RemoteException;

}
