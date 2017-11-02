package lejos.hardware;

import lejos.hardware.device.NewIRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

/**
 * Description: LeJOS NXJ new IRLink driver.
 * Run PF Extended Mode Tests.
 * // RM: It works!
 */

public class IRLinkPF_EM {

    /* Sensors */
    private NewIRLink pfIRLink;

    /* Constructor */
    public IRLinkPF_EM() {
        pfIRLink = new NewIRLink(SensorPort.S2);
    }

    public static void main(String[] args) {
        IRLinkPF_EM obj = new IRLinkPF_EM();
        obj.testPFExtendedMode(); // Run PF Extended Mode Tests.
    }

    // PF Extended Mode Tests.
    public void testPFExtendedMode() {

        LCD.drawString("PF EM:", 0, 0);
        //for (int i=0; i<4; i++) {
        //	pfIRLink.sendPFExtended(i, IRLink.PF_EM_COMMAND_SYNCTOGGLEBIT);
        //}

        while (!(Button.ESCAPE.isDown())) {

            if (Button.ENTER.isDown()) {
                // Stop all motors on output A and toggle output B.
                LCD.drawString("Float A, Tog B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_BRAKEFLOAT_OUTPUT_A);
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B);
                }
            }

            if (Button.RIGHT.isDown()) {
                // PWM: Increase speed on all motors on output A.
                LCD.drawString("Incr A        ", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_INCREMENTSPEED_OUTPUT_A);

                    // Wait some time between each channel.
                    Delay.msDelay(100);
                }
            }

            if (Button.LEFT.isDown()) {
                // PWM: Decrease speed on all motors on output A and toggle output B.
                LCD.drawString("Decr A, Tog B ", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_DECREMENTSPEED_OUTPUT_A);
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B);

                    // Wait some time between each channel.
                    Delay.msDelay(100);
                }
            }
        }
    }
}
