package lejos.hardware;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.ev3.LocalEV3;
import lejos.internal.ev3.EV3DeviceManager;
import lejos.remote.ev3.RemoteEV3;

public class BrickFinder {
	private static final int DEFAULT_PORT = 3016;
	
	public static Brick getLocal() {
		EV3DeviceManager.getLocalDeviceManager();
		return LocalEV3.get();
	}
	
	public static Brick getDefault() throws Exception {
		try {
			EV3DeviceManager.getLocalDeviceManager();
			return LocalEV3.get();
		} catch (Exception e) {

			BrickInfo[] bricks = discover();
			if (bricks.length > 0) {
				return new RemoteEV3(bricks[0].getIPAddress());
			} else {
				throw new Exception("No brick found");
			}
		}
	}

	/**
	 * Search for available EV3s and populate table with results.
	 */
	public static BrickInfo[] discover() throws Exception {	
		
		Map<String,BrickInfo> ev3s = new HashMap<String,BrickInfo>();
		DatagramSocket socket = new DatagramSocket(DEFAULT_PORT);
		socket.setSoTimeout(2000);
        DatagramPacket packet = new DatagramPacket (new byte[100], 100);

        long start = System.currentTimeMillis();
        
        while ((System.currentTimeMillis() - start) < 2000) {
            socket.receive (packet);
            String message = new String(packet.getData(), "UTF-8");
            String ip = packet.getAddress().getHostAddress();
            ev3s.put(ip, new BrickInfo(message.trim(),ip,"EV3"));
        }
        
        if (socket != null) socket.close();
        
        BrickInfo[] devices = new BrickInfo[ev3s.size()];
        int i = 0;
        for(String ev3: ev3s.keySet()) {
        	BrickInfo info = ev3s.get(ev3);
        	devices[i++] = info;
        }
        
        return devices;
	}
}
