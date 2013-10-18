package lejos.remote.ev3;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.hardware.Battery;
import lejos.hardware.ev3.EV3;
import lejos.hardware.port.Port;

public class RemoteEV3 implements EV3 {
	private String host;
	private  RMIEV3 rmiEV3;
	protected ArrayList<RemotePort> ports = new ArrayList<RemotePort>();
	
	public RemoteEV3(String host) throws RemoteException, MalformedURLException, NotBoundException {
		this.host = host;
		rmiEV3 = (RMIEV3) Naming.lookup("//" + host + "/RemoteEV3");
		createPorts();
	}
	
	private void createPorts() {
        // Create the port objects
        ports.add(new RemotePort("S1", RemotePort.SENSOR_PORT, 0, rmiEV3));
        ports.add(new RemotePort("S2", RemotePort.SENSOR_PORT, 1, rmiEV3));
        ports.add(new RemotePort("S3", RemotePort.SENSOR_PORT, 2, rmiEV3));
        ports.add(new RemotePort("S4", RemotePort.SENSOR_PORT, 3, rmiEV3));
        ports.add(new RemotePort("A", RemotePort.MOTOR_PORT, 0, rmiEV3));
        ports.add(new RemotePort("B", RemotePort.MOTOR_PORT, 1, rmiEV3));
        ports.add(new RemotePort("C", RemotePort.MOTOR_PORT, 2, rmiEV3));
        ports.add(new RemotePort("D", RemotePort.MOTOR_PORT, 3, rmiEV3));
	}
	
	@Override
	public Port getPort(String portName) {
        for(RemotePort p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
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
	
	public RMISampleProvider createSampleProvider(String portName, String sensorName, String modeName) {
		try {
			return rmiEV3.createSampleProvider(portName, sensorName, modeName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public RMIRegulatedMotor createRegulatedProvider(String portName) {
		try {
			return rmiEV3.createRegulatedMotor(portName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

}
