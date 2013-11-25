import lejos.remote.ev3.RMILCD;
import lejos.remote.ev3.RemoteEV3;

public class RemoteLCD {
	public static void main(String[] args) throws Exception {
		RemoteEV3 ev3 = new RemoteEV3("192.168.0.9");
		
		RMILCD lcd = ev3.getLCD();
		
		lcd.clearDisplay();
		lcd.drawString("Hello",0,1);
	}
}
