package lejos.robotics;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * This interface defines the methods of a generic ColorDetector object.
 * 
 * @see lejos.hardware.sensor.ColorSensor
 * @see lejos.hardware.sensor.ColorHTSensor
 */
public interface ColorDetector extends ColorIdentifier {

	/**
	 * Return the Red, Green and Blue values together in one object.
	 * @return Color object containing the three RGB component values between 0-255.
	 */
	public Color getColor();
	
}
