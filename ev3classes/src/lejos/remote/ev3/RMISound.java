package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMISound extends Remote {
	
    public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5};
    public final static int[] FLUTE = new int[]{10, 25, 2000, 1000, 25};
    public final static int[] XYLOPHONE = new int[]{1, 8, 3000, 5000, 5};
    
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
	
	public int playSample(String fileName, int vol) throws RemoteException;
	
	public int playSample(String fileName) throws RemoteException;
	
	public int playSample(byte [] data, int offset, int len, int freq, int vol) throws RemoteException;
	
	public void playNote(int[] inst, int freq, int len) throws RemoteException;
	
	public void setVolume(int vol) throws RemoteException;
	
	public int getVolume() throws RemoteException;
}
