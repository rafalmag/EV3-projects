package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.Battery;
import lejos.hardware.port.Port;

public interface RMIEV3 extends Remote {

	public RMIPort getPort(String portName) throws RemoteException;

	public RMIBattery getBattery() throws RemoteException;

}
