package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.PortException;

public class RemoteI2CPort extends RemoteIOPort implements I2CPort {
	protected RMII2CPort rmi;
	protected RMIEV3 rmiEV3;
	
	public RemoteI2CPort(RMIEV3 rmiEV3) {
		this.rmiEV3 = rmiEV3;
	}
	
	public boolean open(int typ, int portNum, RemotePort remotePort) {
        boolean res = super.open(typ,portNum,remotePort);
		try {
			rmi = rmiEV3.openI2CPort(getName());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
		return res;
	}
	
	
	@Override
	public void close() {
		try {
			rmi.close();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int i2cTransaction(int deviceAddress, byte[] writeBuf,
		int writeOffset, int writeLen, byte[] readBuf, int readOffset, int readLen) {
		try {
			byte[] res = rmi.i2cTransaction(deviceAddress, writeBuf, writeOffset, writeLen,  readLen);
			System.out.println("Byte 0 from I2C transaction is " + res[0]);
			System.arraycopy(res, 0, readBuf, readOffset, readLen);
			return (res == null ? - 1 : res.length);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}
