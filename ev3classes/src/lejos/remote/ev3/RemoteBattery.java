package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.Battery;

public class RemoteBattery implements Battery {
	RMIBattery battery;
	
	public RemoteBattery(RMIBattery battery) {
		this.battery = battery;
	}
	
	@Override
	public int getVoltageMilliVolt() {
		try {
			return battery.getVoltageMilliVolt();
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public float getVoltage() {
		try {
			return battery.getVoltage();
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1f;
		}
	}

	@Override
	public float getBatteryCurrent() {
		return 0;
	}

	@Override
	public float getMotorCurrent() {
		return 0;
	}

}
