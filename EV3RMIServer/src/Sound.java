import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Sound extends Remote {
	public void systemSound(boolean aQueued, int aCode) throws RemoteException;
	
	public void beep() throws RemoteException;
	
	public void twoBeeps() throws RemoteException;
	
	public void beepSequence() throws RemoteException;
	
	public void beepSequenceUp() throws RemoteException;
	
	public void buzz() throws RemoteException;
	
	public void pause(int t) throws RemoteException;
	
	public int getTime() throws RemoteException;
	
	public void playTone(int aFrequency, int aDuration, int aVolume) throws RemoteException;
	
	public void playTone(int freq, int duration) throws RemoteException;
	
	public int playSample(File file, int vol) throws RemoteException;
	
	public int playSample(File file) throws RemoteException;
	
	public int playSample(byte [] data, int offset, int len, int freq, int vol) throws RemoteException;
	
	public void playNote(int[] inst, int freq, int len) throws RemoteException;
	
	public void setVolume(int vol) throws RemoteException;
	
	public int getVolume() throws RemoteException;
}
