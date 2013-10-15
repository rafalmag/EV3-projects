package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMenu extends Remote {
	
	public int runProgram(String programName) throws RemoteException;
	
	public int debugProgram(String programName) throws RemoteException;
	
	public int runSample(String programName) throws RemoteException;
	
	public int deleteFile(String fileName) throws RemoteException;
	
	public long getFileSize(String fileName) throws RemoteException;
	
	public String[] getProgramNames() throws RemoteException;
	
	public String[] getSampleNames()  throws RemoteException;
	
	public int uploadFile(String fileName, byte[] contents) throws RemoteException;

}
