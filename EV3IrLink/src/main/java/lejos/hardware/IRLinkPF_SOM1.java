package lejos.hardware;

import lejos.hardware.device.IRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


/** Description: LeJOS NXJ new IRLink driver.
    Run PF Single Output Mode PWM submode Tests.
*/

public class IRLinkPF_SOM1 {
	
	/* Sensors */
	private IRLink pfIRLink;	

	/* Constructor */
	public IRLinkPF_SOM1() {
		pfIRLink = new IRLink(SensorPort.S2);
	}

	public static void main(String[] args) {
		IRLinkPF_SOM1 obj = new IRLinkPF_SOM1();
		obj.testPFSingleOutputMode_SubmodePWM(); // Run PF Single Output Mode PWM submode Tests.
	}

	// PF Single Output Mode PWM submode Tests.
	public void testPFSingleOutputMode_SubmodePWM() {
		byte toggleBit = 0;
		byte pwmSpeed = 0;

		LCD.drawString("PF SOM PWM:", 0, 0);
		while (!(Button.ESCAPE.isDown())) {
	
			if(Button.ENTER.isDown()) {
				// Stop all motors. By brake and float, or just float
				for (int i=0; i<4; i++) {
					if (toggleBit == 0) {
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_A, IRLink.PF_PWM_COMMAND_FLOAT);
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_B, IRLink.PF_PWM_COMMAND_FLOAT);
						LCD.drawString("Float      ", 0, 1);
					} else {
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_A, IRLink.PF_PWM_COMMAND_BRAKEFLOAT);
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_B, IRLink.PF_PWM_COMMAND_FLOAT);
						LCD.drawString("Brake/Float", 0, 1);
					}
					
					// Wait some time between each channel.
					Delay.msDelay(100);
				}
				toggleBit ^= 1;
				
				// Reset PWM speed counter.
				pwmSpeed = 0;
			}

			if(Button.RIGHT.isDown()) {
				// PWM: Increase speed on all motors.
				// Rotate motor in output A forward, and motor in output B backward.
				if (pwmSpeed < 7) {
					pwmSpeed++;
					LCD.drawString("Up to " + pwmSpeed + "    ", 0, 1);
					for (int i=0; i<4; i++) {
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_A, pwmSpeed);
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_B, 15 - (pwmSpeed - 1));
						
						// Wait some time between each channel.
						Delay.msDelay(100);
					}
				}
			}

			if(Button.LEFT.isDown()) {
				// PWM: Decrease speed on all motors.
				// Rotate motor in output A forward, and motor in output B backward.
				if (pwmSpeed > 1) {
					pwmSpeed--;
					LCD.drawString("Down to " + pwmSpeed + "  ", 0, 1);
					for (int i=0; i<4; i++) {
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_A, pwmSpeed);
						pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_PWM, IRLink.PF_SOM_OUTPUT_B, 15 - (pwmSpeed - 1));
						
						// Wait some time between each channel.
						Delay.msDelay(100);
					}
				}
			}
        }
    }
}
