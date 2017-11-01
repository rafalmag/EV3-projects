package lejos.hardware;

import lejos.hardware.device.IRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

/** Description: This is empty LeJOS NXJ project.
   Play tones and music on RCX via IRLink.
*/

public class IRLinkRCX_Moto {
	
	/* Sensors */	
	private IRLink rcxIRLink;

	/* Constructor */
	public IRLinkRCX_Moto() {
		rcxIRLink = new IRLink(SensorPort.S3);
	}

	public static void main(String[] args) {
		IRLinkRCX_Moto obj = new IRLinkRCX_Moto();
		obj.testRCXMotorCommands(); // RCX - Send commands to Motors
	}

	// RCX - Send commands to Motors
	public void testRCXMotorCommands() {
		boolean toggleOnOff = false;
		boolean toggleDir = false;
		int power = 1;

		LCD.drawString("RCX: COMMAND", 0, 0);		
		rcxIRLink.setDebugMode(true);
		LCD.drawString("Batt: " + rcxIRLink.getBatteryLevel(), 0, 3);

		while (!(Button.ESCAPE.isDown())) {

			if(Button.ENTER.isDown()) {
				if (toggleOnOff) {
					LCD.drawString("RCX: M Start   ", 0, 1);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_A, IRLink.RCX_MOTOR_START);
					Delay.msDelay(500);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_B, IRLink.RCX_MOTOR_START);
					Delay.msDelay(500);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_C, IRLink.RCX_MOTOR_START);
					Delay.msDelay(500);
					rcxIRLink.playSystemSound(IRLink.RCX_SYSTEMSOUND_SWEEPUP);

				} else {
					LCD.drawString("RCX: M Stop    ", 0, 1);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_A, IRLink.RCX_MOTOR_STOP);
					Delay.msDelay(500);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_B, IRLink.RCX_MOTOR_STOP);
					Delay.msDelay(500);
					rcxIRLink.motorStartStop(IRLink.RCX_MOTOR_C, IRLink.RCX_MOTOR_STOP);
					Delay.msDelay(500);
					rcxIRLink.playSystemSound(IRLink.RCX_SYSTEMSOUND_SWEEPDOWN);
				}
				toggleOnOff = !toggleOnOff;
			}

			if(Button.RIGHT.isDown()) {
				if (toggleDir) {
					LCD.drawString("RCX: M Forward ", 0, 1);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_A, IRLink.RCX_MOTOR_BACKWARD);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_B, IRLink.RCX_MOTOR_BACKWARD);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_C, IRLink.RCX_MOTOR_BACKWARD);
					Delay.msDelay(500);
				} else {
					LCD.drawString("RCX: M Forward ", 0, 1);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_A, IRLink.RCX_MOTOR_FORWARD);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_B, IRLink.RCX_MOTOR_FORWARD);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_C, IRLink.RCX_MOTOR_FORWARD);
					Delay.msDelay(500);
				}
				toggleDir = !toggleDir;
			}

			if(Button.LEFT.isDown()) {
					/*
					LCD.drawString("RCX: M Reverse ", 0, 1);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_A, IRLink.RCX_MOTOR_REVERSE);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_B, IRLink.RCX_MOTOR_REVERSE);
					Delay.msDelay(500);
					rcxIRLink.motorDirection(IRLink.RCX_MOTOR_C, IRLink.RCX_MOTOR_REVERSE);
					Delay.msDelay(500);
					*/
					LCD.drawString("RCX: M Power   ", 0, 1);
					rcxIRLink.motorPower(IRLink.RCX_MOTOR_A, power);
					Delay.msDelay(500);
					rcxIRLink.motorPower(IRLink.RCX_MOTOR_B, power);
					Delay.msDelay(500);
					rcxIRLink.motorPower(IRLink.RCX_MOTOR_C, power);
					Delay.msDelay(500);
					power = ++power % 7;
			}
		}
	}
}
