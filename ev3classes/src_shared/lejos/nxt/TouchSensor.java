package lejos.nxt;
import lejos.robotics.Touch;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Abstraction for a NXT touch sensor.
 * Also works with RCX touch sensors.
 * 
 */
public class TouchSensor extends AnalogSensor implements SensorConstants, Touch {
	
	/**
	 * Create a touch sensor object attached to the specified open port. Note this
	 * port will not be configured. Any configuration od the sensor port must take
	 * place externally.
	 * @param open an open Analog port
	 */
	public TouchSensor(AnalogPort port)
	{
	   super(port);
	   port.setTypeAndMode(TYPE_SWITCH, MODE_BOOLEAN);
	}

	/**
	 * Create an NXT touch sensor object attached to the specified port.
	 * @param port the port that has the sensor attached
	 */
	public TouchSensor(Port port)
	{
	    super(port);
	    this.port.setTypeAndMode(TYPE_SWITCH, MODE_BOOLEAN);	    
	}
	/**
	 * Check if the sensor is pressed.
	 * @return <code>true</code> if sensor is pressed, <code>false</code> otherwise.
	 */
	public boolean isPressed()
	{
		return (port.readRawValue() < 600);  
	}
}
