import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;


public class FindBrick {
	public static void main(String[] args) {
		Brick brick = BrickFinder.getDefault();
		
		System.out.println("Found brick: " + brick.getName() + ", type = " + brick.getType());
	}
}
