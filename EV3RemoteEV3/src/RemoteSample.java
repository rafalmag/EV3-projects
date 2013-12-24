import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

public class RemoteSample {

	public static void main(String[] args) throws Exception {
		RemoteEV3 ev3 = new RemoteEV3("192.168.0.9");
		
		RMISampleProvider sp = ev3.createSampleProvider("S1", "lejos.hardware.sensor.EV3IRSensor", "distance");
		
		float[] sample = sp.fetchSample();
		
		System.out.println("Sample");
		for(float data: sample) {
			System.out.println("  " + data);
		}
		
		sp.close();

		RMIRegulatedMotor m = ev3.createRegulatedMotor("A");
		
		m.rotate(360);
		m.rotate(-360);
		
		m.close();
	}

}
