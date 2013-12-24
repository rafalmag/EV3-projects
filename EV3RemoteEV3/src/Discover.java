import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteEV3;


public class Discover {

	public static void main(String[] args) throws Exception {
		BrickInfo[] bricks = BrickFinder.discover();
		
		for(BrickInfo info: bricks) {
			Brick brick = new RemoteEV3(info.getIPAddress());
			brick.getAudio().systemSound(0);
		}
	}
}
