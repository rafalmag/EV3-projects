import java.util.Set;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;


public class FindBrick {
	public static void main(String[] args) {
		try {
			Brick brick = BrickFinder.getDefault();
			
			System.out.println("Found: " + brick.getName() + " , type: " + brick.getType());
		} catch (Exception e) {
			System.err.println("Failed to find a brick: " + e);
		}
	}
}
