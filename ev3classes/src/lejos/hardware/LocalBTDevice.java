package lejos.hardware;

import java.io.IOException;
import java.util.Collection;

import com.sun.jna.LastErrorException;

import lejos.internal.io.NativeHCI;

public class LocalBTDevice {
	private NativeHCI hci = new NativeHCI();
	
	public Collection<RemoteBTDevice> search() throws IOException {
		try {
			return hci.hciInquiry();
		} catch (LastErrorException e) {
			throw(new IOException(e.getMessage()));
		}
	}
	
	public void setVisibility(boolean visible) throws IOException {
		try {
			hci.hciSetVisible(visible);
		} catch (LastErrorException e) {
			throw(new IOException(e.getMessage()));
		}
	}
	
	public boolean getVisibility() {
		return hci.hcigetVisible();
	}
	
	public static boolean isPowerOn() {
		return true;
	}
	
	public String getBluetoothAddress() {
		StringBuilder sb = new StringBuilder();
		for(int j=5;j>=0;j--) {
			String hex = Integer.toHexString(getDeviceInfo() .bdaddr[j] & 0xFF).toUpperCase();
			if (hex.length() == 1) sb.append('0');
			sb.append(hex);
			if (j>0) sb.append(':');
		}
		return sb.toString();
	}
	
	public String getFriendlyName() {
		return "EV3";
	}
	
	public NativeHCI.DeviceInfo getDeviceInfo() {
		return hci.hciGetDeviceInfo();
	}
}
