import lejos.hardware.Bluetooth;
import lejos.hardware.sensor.AccelHTSensor;
import lejos.hardware.sensor.LightSensor;
import lejos.hardware.sensor.TouchSensor;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.RemoteNXT;
import lejos.remote.nxt.NXT;
import lejos.remote.nxt.RemoteNXTAnalogPort;

public class NXTRemote {

	private static final String NXT = "00:16:53:12:92:AA";
	
	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		NXT nxt = RemoteNXT.get(NXT, connector);
		
		System.out.println("Battery voltage is " + nxt.getBattery().getVoltage());
		
		LightSensor light = new LightSensor(nxt.getPort("S1").open(RemoteNXTAnalogPort.class));
		
		System.out.println("Light sensor : " + light.getLightValue());

		TouchSensor touch = new TouchSensor(nxt.getPort("S2"));
		
		System.out.println("Touch sensor isPressed: " + touch.isPressed());
		
		AccelHTSensor accel = new AccelHTSensor(nxt.getPort("S3"));
		
		System.out.println("X acceleration: " + accel.getXAccel());
	}

}
