package lejos.ev3.startup;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.remote.ev3.Menu;
import lejos.remote.ev3.RMIMenu;

public class RMIRemoteMenu extends UnicastRemoteObject implements RMIMenu {
	private Menu menu;
	
	private static final long serialVersionUID = 9132686914626791288L;

	protected RMIRemoteMenu(Menu menu) throws RemoteException {
		super(0);
		this.menu = menu;
	}

	@Override
	public int runProgram(String programName) throws RemoteException {
		return menu.runProgram(programName);
	}

	@Override
	public int deleteFile(String fileName) throws RemoteException {
		return menu.deleteFile(fileName);
	}

	@Override
	public String[] getProgramNames() throws RemoteException {
		return menu.getProgramNames();
	}

	@Override
	public int debugProgram(String programName) throws RemoteException {
		return menu.debugProgram(programName);
	}

	@Override
	public int runSample(String programName) throws RemoteException {
		return menu.runSample(programName);
	}

	@Override
	public String[] getSampleNames() throws RemoteException {
		return menu.getSampleNames();
	}
	
	public long getFileSize(String filename) {
		return menu.getFileSize(filename);
	}

	@Override
	public int uploadFile(String fileName, byte[] contents)
			throws RemoteException {
		return menu.uploadFile(fileName, contents);
	}

	@Override
	public byte[] fetchFile(String fileName) throws RemoteException {
		return menu.fetchFile(fileName);
	}

	@Override
	public String getSetting(String setting) throws RemoteException {
		return menu.getSetting(setting);
	}

	@Override
	public boolean setSetting(String setting, String value)
			throws RemoteException {
		return menu.setSetting(setting, value);
	}

	@Override
	public boolean deleteAllPrograms() throws RemoteException {
		return menu.deleteAllPrograms();
	}

	@Override
	public String getVersion() throws RemoteException {
		return menu.getVersion();
	}

	@Override
	public String getMenuVersion() throws RemoteException {
		return menu.getMenuVersion();
	}

	@Override
	public String getName() throws RemoteException {
		return menu.getName();
	}

	@Override
	public boolean setName(String name) throws RemoteException {
		return menu.setName(name);
	}

}
