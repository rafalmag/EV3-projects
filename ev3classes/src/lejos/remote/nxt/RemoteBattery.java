package lejos.remote.nxt;

import java.io.*;

import lejos.hardware.Battery;


/**
 * Battery readings from a remote NXT.
 */
public class RemoteBattery implements Battery, NXTProtocol {
	
	private NXTCommand nxtCommand;
	
	public RemoteBattery(NXTCommand nxtCommand) {
		this.nxtCommand = nxtCommand;
	}
		
	/**
	 * The NXT uses 6 batteries of 1500 mV each.
	 * @return Battery voltage in mV. ~9000 = full.
	 */
	public int getVoltageMilliVolt() {
		/*
	     * calculation from LEGO firmware
	     */
		try {
			return nxtCommand.getBatteryLevel();
		} catch (IOException ioe) {
			return 0;
		}
	}

	/**
	 * The NXT uses 6 batteries of 1.5 V each.
	 * @return Battery voltage in Volt. ~9V = full.
	 */
	public float getVoltage() {
	   return (float)(getVoltageMilliVolt() * 0.001);
	}

	@Override
	public float getBatteryCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMotorCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}
}

