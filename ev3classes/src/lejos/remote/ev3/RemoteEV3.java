package lejos.remote.ev3;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Battery;
import lejos.hardware.ev3.EV3;
import lejos.hardware.port.Port;

public class RemoteEV3 implements EV3 {
	private String host;
	private  RMIEV3 rmiEV3;
	
	public RemoteEV3(String host) throws RemoteException, MalformedURLException, NotBoundException {
		this.host = host;
		rmiEV3 = (RMIEV3) Naming.lookup("//" + host + "/RemoteEV3");
	}
	
	@Override
	public Port getPort(String portName) {
		return null;
	}

	@Override
	public Battery getBattery() {
		try {
			return new RemoteBattery(rmiEV3.getBattery());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getHost() {
		return host;
	}

}
