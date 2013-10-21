package lejos.hardware;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import com.sun.jna.LastErrorException;

import lejos.internal.io.NativeHCI;

public class LocalBTDevice {
	private NativeHCI hci = new NativeHCI();
	private HashMap<String,String> knownDevices = new HashMap<String,String>();
	private Properties props = new Properties();
	private FileReader fr;
	
	public LocalBTDevice() {
		try {
			fr = new FileReader("/home/root/lejos/nxj.cache");
			props.load(fr);
		    Enumeration<String> e = (Enumeration<String>) props.propertyNames();

		    while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      if (key.startsWith("NXT_")) {
		    	  knownDevices.put(props.getProperty(key), key.substring(4));
		      }
		    }
		} catch (IOException e) {
			System.out.println("Failed to load nxj.cache: " + e);
		}
	}
	public Collection<RemoteBTDevice> search() throws IOException {
		try {
			Collection<RemoteBTDevice> results = hci.hciInquiry();
			for(RemoteBTDevice d: results) {
				knownDevices.put(d.getName(), d.getAddress());
			}
			return results;
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
	
	private void saveKnownDevices() {
		Set<String> keys = knownDevices.keySet();
		
	}
}
