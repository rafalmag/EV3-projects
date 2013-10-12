import lejos.hardware.ev3.EV3;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.sensor.AccelMindSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.LightSensor;
import lejos.remote.ev3.RemoteAnalogPort;
import lejos.remote.ev3.RemoteEV3;


public class RemoteTest {
	public static void main(String[] args) throws Exception {
		EV3 ev3 = new RemoteEV3("192.168.0.9");
		
		System.out.println("Battery voltage: " + ev3.getBattery().getVoltage());

		AnalogPort port = ev3.getPort("S1").open(RemoteAnalogPort.class);
		LightSensor light = new LightSensor(port);
		
		System.out.println("Light value is " + light.getLightValue());
		
		AccelMindSensor accel = new AccelMindSensor(ev3.getPort("S2"));
		
		EV3IRSensor ir = new EV3IRSensor(ev3.getPort("S3"));
		
		System.out.println("X Acceleration is " + accel.getXAccel());
		
		System.out.println("Distance  is " + ir.getRange());
		
		port.close();
		
		accel.close();
		
		ir.close();
		
	}
}
