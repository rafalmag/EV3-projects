package lejos.remote.ev3;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.Sound;


public class RMIRemoteSound extends UnicastRemoteObject implements RMISound {

	private static final long serialVersionUID = -8852558267893400316L;

	protected RMIRemoteSound() throws RemoteException {
		super(0);
	}

	@Override
	public void systemSound(boolean aQueued, int aCode) throws RemoteException {
		Sound.systemSound(aQueued, aCode);
	}

	@Override
	public void beep() throws RemoteException {
		Sound.beep();
	}

	@Override
	public void twoBeeps() throws RemoteException {
		Sound.twoBeeps();
	}

	@Override
	public void beepSequence() throws RemoteException {
		Sound.beepSequence();
	}

	@Override
	public void beepSequenceUp() throws RemoteException {
		Sound.beepSequenceUp();
	}

	@Override
	public void buzz() throws RemoteException {
		Sound.buzz();	
	}

	@Override
	public void pause(int t) throws RemoteException {
		Sound.pause(t);	
	}

	@Override
	public int getTime() throws RemoteException {
		return Sound.getTime();
	}

	@Override
	public void playTone(int aFrequency, int aDuration, int aVolume)
			throws RemoteException {
		Sound.playTone(aFrequency, aDuration, aVolume);	
	}

	@Override
	public void playTone(int freq, int duration) throws RemoteException {
		Sound.playTone(freq, duration);
	}

	@Override
	public int playSample(File file, int vol) throws RemoteException {
		return Sound.playSample(file, vol);
	}

	@Override
	public int playSample(File file) throws RemoteException {
		return Sound.playSample(file);
	}

	@Override
	public int playSample(byte[] data, int offset, int len, int freq, int vol)
			throws RemoteException {
		return Sound.playSample(data, offset, len, freq, vol);
	}

	@Override
	public void playNote(int[] inst, int freq, int len) throws RemoteException {
		Sound.playNote(inst, freq, len);
	}

	@Override
	public void setVolume(int vol) throws RemoteException {
		Sound.setVolume(vol);
	}

	@Override
	public int getVolume() throws RemoteException {
		return Sound.getVolume();
	}
}
