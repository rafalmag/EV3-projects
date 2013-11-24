import java.io.File;
import java.io.FileInputStream;

import lejos.remote.ev3.RMISound;
import lejos.remote.ev3.RemoteEV3;

/**
 * Test of remote sound
 * 
 * @author Lawrie Griffiths
 *
 */
public class RemoteSound {
	
	public static void main(String[] args) throws Exception {
		RemoteEV3 ev3 = new RemoteEV3("192.168.0.9");
		
		RMISound sound = ev3.getSound();
		
		sound.beep();
		sound.pause(1000);
		sound.beepSequence();
		sound.pause(1000);
		sound.beepSequenceUp();
		sound.pause(1000);
		sound.buzz();
		sound.pause(1000);
		
		for(int i=0;i<5;i++) {
			sound.systemSound(false, i);
			sound.pause(1000);
		}
		
		sound.playTone(500, 2000, 100);
		
		sound.playNote(RMISound.PIANO, 2000, 1000);
		
		File f = new File("Trumpet.wav");
		FileInputStream in = new FileInputStream(f);
		byte[] data = new byte[(int)f.length()];
	    in.read(data);
	    in.close();
	    sound.playSample(data, 0, (int) f.length(), 8000, 100);
	}
}
