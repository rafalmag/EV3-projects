import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteSound extends UnicastRemoteObject implements Sound {

	private static final long serialVersionUID = -8852558267893400316L;

	protected RemoteSound() throws RemoteException {
		super(0);
	}

	@Override
	public void systemSound(boolean aQueued, int aCode) throws RemoteException {
		lejos.nxt.Sound.systemSound(aQueued, aCode);
	}

	@Override
	public void beep() throws RemoteException {
		lejos.nxt.Sound.beep();
	}

	@Override
	public void twoBeeps() throws RemoteException {
		lejos.nxt.Sound.twoBeeps();
	}

	@Override
	public void beepSequence() throws RemoteException {
		lejos.nxt.Sound.beepSequence();
	}

	@Override
	public void beepSequenceUp() throws RemoteException {
		lejos.nxt.Sound.beepSequenceUp();
	}

	@Override
	public void buzz() throws RemoteException {
		lejos.nxt.Sound.buzz();	
	}

	@Override
	public void pause(int t) throws RemoteException {
		lejos.nxt.Sound.pause(t);	
	}

	@Override
	public int getTime() throws RemoteException {
		return lejos.nxt.Sound.getTime();
	}

	@Override
	public void playTone(int aFrequency, int aDuration, int aVolume)
			throws RemoteException {
		lejos.nxt.Sound.playTone(aFrequency, aDuration, aVolume);	
	}

	@Override
	public void playTone(int freq, int duration) throws RemoteException {
		lejos.nxt.Sound.playTone(freq, duration);
	}

	@Override
	public int playSample(File file, int vol) throws RemoteException {
		return lejos.nxt.Sound.playSample(file, vol);
	}

	@Override
	public int playSample(File file) throws RemoteException {
		return lejos.nxt.Sound.playSample(file);
	}

	@Override
	public int playSample(byte[] data, int offset, int len, int freq, int vol)
			throws RemoteException {
		return lejos.nxt.Sound.playSample(data, offset, len, freq, vol);
	}

	@Override
	public void playNote(int[] inst, int freq, int len) throws RemoteException {
		lejos.nxt.Sound.playNote(inst, freq, len);
	}

	@Override
	public void setVolume(int vol) throws RemoteException {
		lejos.nxt.Sound.setVolume(vol);
	}

	@Override
	public int getVolume() throws RemoteException {
		return lejos.nxt.Sound.getVolume();
	}
}
