import java.util.Collection;

import lejos.hardware.Bluetooth;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;
import lejos.internal.io.NativeHCI;

public class Search {
	static NativeHCI.DeviceInfo deviceInfo;
	
	public static void main(String[] args) throws Exception {	
		LocalBTDevice bt = Bluetooth.getLocalDevice();
		
		System.out.println("Local address is " + bt.getBluetoothAddress());
		
		System.out.println("Searching ...\n");
		
		Collection<RemoteBTDevice>  devices = bt.search();
		for(RemoteBTDevice device: devices) {
			System.out.println(device.getName() + "\t" + device.getAddress());
		}

		System.out.println("\nSearch complete");
		
		//bt.setVisibility(true);
		
		deviceInfo = bt.getDeviceInfo();
	
		// Check for PISCAN
		if ((deviceInfo.flags & 0x18) == 0x18) System.out.println("Visible");
		else System.out.println("Not visible");
		
		printDeviceInfo();
	}
	
	private static void printDeviceInfo() {
		System.out.println("Device id = " + deviceInfo.dev_id);
		System.out.println("Flags = " + deviceInfo.flags);
	}
}
