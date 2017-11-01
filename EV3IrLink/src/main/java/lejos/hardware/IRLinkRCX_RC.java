package lejos.hardware;

import lejos.hardware.device.IRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

/** Description: This is empty LeJOS NXJ project.
   Play tones and music on RCX via IRLink.
*/

public class IRLinkRCX_RC {
	
	/* Sensors */	
	private IRLink rcxIRLink;

	/* Constructor */
	public IRLinkRCX_RC() {
		rcxIRLink = new IRLink(SensorPort.S3);
	}

	public static void main(String[] args) {
		IRLinkRCX_RC obj = new IRLinkRCX_RC();
		obj.testRCXRemote(); // RCX - Remote Control Tests
	}

	// RCX - Remote Control Tests
	public void testRCXRemote() {
		byte[] buf = new byte[3];
		
		LCD.drawString("RCX: REMOTE", 0, 0);
		while (!(Button.ESCAPE.isDown())) {

			if(Button.ENTER.isDown()) {
				// Stop all motors on output A and output B.
				LCD.drawString("RCX Beep    ", 0, 1);
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_NOOP); // Must preceed every command except motor command 
				rcxIRLink.beep();
			}

			if(Button.RIGHT.isDown()) {
				// Full forward motors on output A and full backward motors on output B.
				LCD.drawString("RCX Forward ", 0, 1);
				for (int i=0; i<3; i++) {
					rcxIRLink.forwardStep(i);
				}
				/*
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_A_FWD);
				Delay.msDelay(500);
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_B_FWD);
				Delay.msDelay(500);
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_C_FWD);
				Delay.msDelay(500);
				*/
			}

			if(Button.LEFT.isDown()) {
				// Full backward motors on output A and full forwardard motors on output B.
				LCD.drawString("RCX Backward", 0, 1);
				for (int i=0; i<3; i++) {
					rcxIRLink.backwardStep(i);
				}
				/*				
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_A_BWD);
				Delay.msDelay(500);
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_B_BWD);
				Delay.msDelay(500);
				rcxIRLink.sendRemoteCommand(IRLink.RCX_REMOTE_C_BWD);
				Delay.msDelay(500);
				*/
			}
		}
	}
}
