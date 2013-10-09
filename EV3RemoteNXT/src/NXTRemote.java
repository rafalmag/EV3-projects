import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.RemoteNXT;
import lejos.remote.nxt.NXT;

public class NXTRemote {

	private static final String NXT = "00:16:53:12:92:AA";
	
	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		NXT nxt = RemoteNXT.get(NXT, connector);
		
		System.out.println("Battery voltage is " + nxt.getBattery().getVoltage());

	}

}
