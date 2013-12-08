import lejos.hardware.Bluetooth;
import lejos.remote.nxt.RemoteNXT;


public class NXTRemote {

	public static void main(String[] args) throws Exception {
		RemoteNXT nxt = new RemoteNXT("NXT2", Bluetooth.getNXTCommConnector());
		
		System.out.println(nxt.getName());
		
		nxt.getAudio().playTone(1000, 1000);
		
		System.exit(0);

	}

}
