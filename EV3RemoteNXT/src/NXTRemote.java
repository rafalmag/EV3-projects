import lejos.hardware.Bluetooth;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.sensor.AccelHTSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.RCXLightSensor;
import lejos.hardware.sensor.TouchSensor;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.RemoteNXT;
import lejos.remote.nxt.NXT;
import lejos.remote.nxt.RemoteNXTAnalogPort;
import lejos.utility.Delay;

public class NXTRemote {

	private static final String NXT = "00:16:53:12:92:AA";
	
	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		NXT nxt = RemoteNXT.get(NXT, connector);
		
		System.out.println("Battery voltage is " + nxt.getBattery().getVoltage());
		
		NXTLightSensor light = new NXTLightSensor(nxt.getPort("S1").open(RemoteNXTAnalogPort.class));
		
		RCXLightSensor rcxLight = new RCXLightSensor(nxt.getPort("S4").open(RemoteNXTAnalogPort.class));
		
		//System.out.println("Light sensor : " + light.getLightValue());
		
		System.out.println("RCX Light sensor : " + rcxLight.getLightValue());

		TouchSensor touch = new TouchSensor(nxt.getPort("S2"));
		
		System.out.println("Touch sensor isPressed: " + touch.isPressed());
		
		AccelHTSensor accel = new AccelHTSensor(nxt.getPort("S3"));
		
		System.out.println("X acceleration: " + accel.getXAccel());
		
		NXTMotor m = new NXTMotor(nxt.getPort("A"));
		
		m.forward();
		
		m.setPower(50);;
		
		Delay.msDelay(5000);
		
		m.stop();
		
	}

}
