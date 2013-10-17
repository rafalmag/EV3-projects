import lejos.hardware.ev3.EV3;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;


public class RemoteSample {

	public static void main(String[] args) throws Exception {
		RemoteEV3 ev3 = new RemoteEV3("192.168.0.9");
		
		RMISampleProvider sp = ev3.getSampleProvider("S1", "NXTTUltrasonicSensor", null);
		
		float[] sample = sp.fetchSample();
		
		System.out.println("Sample");
		for(float data: sample) {
			System.out.println("  " + sample);
		}

	}

}
