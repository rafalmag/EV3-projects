package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.port.IOPort;

public interface RMIPort extends Remote {

	public String getName() throws RemoteException;

	public <T extends IOPort> T open(Class<T> portclass) throws RemoteException;

}
