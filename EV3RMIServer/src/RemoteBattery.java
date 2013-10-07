import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;

public class RemoteBattery extends UnicastRemoteObject implements Battery {

	protected RemoteBattery() throws RemoteException {
		super(0);
	}

	@Override
	public int getVoltageMilliVolt() throws RemoteException {
		return LocalEV3.get().getBattery().getVoltageMilliVolt();
	}

	@Override
	public float getVoltage() throws RemoteException {
		return LocalEV3.get().getBattery().getVoltage();
	}

	@Override
	public float getBatteryCurrent() throws RemoteException {
		return LocalEV3.get().getBattery().getBatteryCurrent();
	}

	@Override
	public float getMotorCurrent() throws RemoteException {
		return LocalEV3.get().getBattery().getMotorCurrent();
	}

}
