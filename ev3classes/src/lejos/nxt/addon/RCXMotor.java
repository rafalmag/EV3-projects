package lejos.nxt.addon;

import lejos.nxt.BasicMotor;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.Port;

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
