import lejos.remote.ev3.RemoteEV3;

public class RemoteWifi {

	public static void main(String[] args) throws Exception {
		RemoteEV3 ev3 = new RemoteEV3("192.168.0.9");
		
		String[] aps = ev3.getWifi().getAccessPointNames();
		
		for(String ap : aps) {
			System.out.println(ap);
		}
	}
}
