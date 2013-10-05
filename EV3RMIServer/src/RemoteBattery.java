import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.nxt.LocalEV3;

public class RemoteBattery extends UnicastRemoteObject implements Battery {

	protected RemoteBattery() throws RemoteException {
		super(0);
	}

	@Override
	public int getVoltageMilliVolt() throws RemoteException {
		return LocalEV3.battery().getVoltageMilliVolt();
	}

	@Override
	public float getVoltage() throws RemoteException {
		return LocalEV3.battery().getVoltage();
	}

	@Override
	public float getBatteryCurrent() throws RemoteException {
		return LocalEV3.battery().getBatteryCurrent();
	}

	@Override
	public float getMotorCurrent() throws RemoteException {
		return LocalEV3.battery().getMotorCurrent();
	}

}
