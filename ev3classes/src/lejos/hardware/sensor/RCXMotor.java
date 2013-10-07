package lejos.hardware.sensor;

import lejos.hardware.motor.BasicMotor;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.Port;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Abstraction for an RCX motor.
 * 
 */
public class RCXMotor extends BasicMotor {
    
	public RCXMotor(BasicMotorPort port)
	{
		this.port = port;
	}
	
	public RCXMotor(Port port)
	{
	    this(port.open(BasicMotorPort.class));
	    releaseOnClose(this.port);
	}

}
