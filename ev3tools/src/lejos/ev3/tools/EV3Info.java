package lejos.ev3.tools;

public class EV3Info {
	private String name;
	private String ipAddress;
	
	public EV3Info(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}

	
	public String getName() {
		return name;
	}
	
	public String getIPAddress() {
		return ipAddress;
	}
}
