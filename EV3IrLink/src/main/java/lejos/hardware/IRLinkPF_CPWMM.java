package lejos.hardware;


import lejos.hardware.device.IRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;

/** Description: LeJOS NXJ new IRLink driver.
    Run PF Combo PWM Mode Tests.
*/

public class IRLinkPF_CPWMM {
	
	/* Sensors */
	private IRLink pfIRLink;

	/* Constructor */
	public IRLinkPF_CPWMM() {
		pfIRLink = new IRLink(SensorPort.S2);
	}

	public static void main(String[] args) {
		IRLinkPF_CPWMM obj = new IRLinkPF_CPWMM();
		obj.testPFComboPWMMode(); // Run PF Combo PWM Mode Tests.
	}

	// PF Combo PWM Mode Tests.
	public void testPFComboPWMMode() {
		byte pwmSpeed = 0;
		
		LCD.drawString("PF CPWMM:", 0, 0);
		while (!(Button.ESCAPE.isDown())) {

			if(Button.ENTER.isDown()) {
				// Stop all motors on output A and output B.
				LCD.drawString("Fl A, Fl B", 0, 1);
				for (int i=0; i<4; i++) {
					pfIRLink.sendPFComboPWM(i, IRLink.PF_PWM_COMMAND_FLOAT, IRLink.PF_PWM_COMMAND_FLOAT);
				}
				pwmSpeed = 0;
			}

			if(Button.RIGHT.isDown()) {
				// Full forward motors on output A and full backward motors on output B.
				LCD.drawString("FF A, FB B", 0, 1);
				pwmSpeed++;
				for (int i=0; i<4; i++) {
					pfIRLink.sendPFComboPWM(i, pwmSpeed, pwmSpeed);
				}
			}

			if(Button.LEFT.isDown()) {
				// Full backward motors on output A and full forward motors on output B.
				LCD.drawString("FB A, FF B", 0, 1);
				pwmSpeed--;
				for (int i=0; i<4; i++) {
					pfIRLink.sendPFComboPWM(i, pwmSpeed, pwmSpeed);
				}
			}
		}
	}
}

