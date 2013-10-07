package lejos.hardware.port;

import lejos.hardware.port.AnalogPort;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Abstraction for a port that supports legacy RCX sensors.
 * 
 * @author Lawrie Griffiths.
 *
 */
public interface LegacySensorPort extends AnalogPort {
	public void activate();	
	public void passivate();
}
