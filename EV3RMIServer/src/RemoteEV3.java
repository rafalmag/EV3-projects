import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.remote.ev3.RMIBattery;
import lejos.remote.ev3.RMIEV3;
import lejos.remote.ev3.RMIPort;


public class RemoteEV3 extends UnicastRemoteObject implements RMIEV3 {

	private static final long serialVersionUID = -6637513883001761328L;

	protected RemoteEV3() throws RemoteException {
		super(0);
	}

	@Override
	public RMIPort getPort(String portName) throws RemoteException {
		return null;
	}

	@Override
	public RMIBattery getBattery() throws RemoteException {
		return new RemoteBattery();
	}

}
