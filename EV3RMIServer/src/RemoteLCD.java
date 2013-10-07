import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteLCD extends UnicastRemoteObject implements LCD {

	private static final long serialVersionUID = 5282037053480160542L;

	protected RemoteLCD() throws RemoteException {
		super(0);
	}

	@Override
	public void drawChar(char c, int x, int y) throws RemoteException {
		lejos.hardware.LCD.drawChar(c, x, y);
	}

	@Override
	public void clearDisplay() throws RemoteException {
		lejos.hardware.LCD.clearDisplay();
	}

	@Override
	public void drawString(String str, int x, int y, boolean inverted)
			throws RemoteException {
		lejos.hardware.LCD.drawString(str, x, y, inverted);
	}

	@Override
	public void drawString(String str, int x, int y) throws RemoteException {
		lejos.hardware.LCD.drawString(str, x, y);
	}

	@Override
	public void drawInt(int i, int x, int y) throws RemoteException {
		lejos.hardware.LCD.drawInt(i, x, y);	
	}

	@Override
	public void drawInt(int i, int places, int x, int y) throws RemoteException {
		lejos.hardware.LCD.drawInt(i, places, x, y);	
	}

	@Override
	public void asyncRefresh() throws RemoteException {
		lejos.hardware.LCD.asyncRefresh();
		
	}

	@Override
	public void refresh() throws RemoteException {
		lejos.hardware.LCD.refresh();
	}

	@Override
	public void clear() throws RemoteException {
		lejos.hardware.LCD.clear();
	}

}
