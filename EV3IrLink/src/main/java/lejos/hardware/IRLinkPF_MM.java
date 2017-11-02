package lejos.hardware;

import lejos.hardware.device.NewIRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


/**
 * Description: LeJOS NXJ new IRLink driver.
 * Run Mixed mode tests: PF Single Output Mode with Extended Mode, to present toggle bit importance.
 */

public class IRLinkPF_MM {

    /* Sensors */
    private NewIRLink pfIRLink;

    /* Constructor */
    public IRLinkPF_MM() {
        pfIRLink = new NewIRLink(SensorPort.S2);
    }

    public static void main(String[] args) {
        IRLinkPF_MM obj = new IRLinkPF_MM();
        obj.testMixedEMandSOM(); // Run Mixed mode tests: PF Single Output Mode with Extended Mode, to present toggle bit importance.
    }

    // PF Mixed Extended Mode and Single Output Mode Tests (Toggle Bit aware modes).
    public void testMixedEMandSOM() {
        LCD.drawString("PF EM/SOM:", 0, 0);
        while (!(Button.ESCAPE.isDown())) {

            if (Button.RIGHT.isDown()) {
                // EM PWM: Increase speed on all motors on output A
                // SOM PWM: Backward 7 on B
                //	or
                // SOM CSTID: Toggle Full Backward on B
                // EM PWM: Toggle output B

                LCD.drawString("Inc A, FB B, Tog B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    // EM: Send toggle bit 0, next to 1
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_INCREMENTSPEED_OUTPUT_A);
                    // SOM: Change of mode, toggle bit reset?
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_PWM, NewIRLink.PF_SOM_OUTPUT_B, NewIRLink.PF_PWM_COMMAND_BACKWARD_7);
//					pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_CSTID, IRLink.PF_SOM_OUTPUT_B, IRLink.PF_CSTID_COMMAND_TOGGLEFULLBACKWARD);
                }
                // Wait some time
                Delay.msDelay(3000);
                for (int i = 0; i < 4; i++) {
                    // EM: Send toggle bit 1, next to 0. Will this work?
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B);
                }

            }

            if (Button.LEFT.isDown()) {
                // EM PWM: Decrease speed on all motors on output A
                // SOM PWM: Backward 7 on B
                //	or
                // SOM CSTID: Toggle Full Backward on B
                // EM PWM: Toggle output B
                LCD.drawString("Dec A, FB B, Tog B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    // EM: Send toggle bit 0, next to 1
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_DECREMENTSPEED_OUTPUT_A);
                    // SOM: Change of mode, toggle bit reset?
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_PWM, NewIRLink.PF_SOM_OUTPUT_B, NewIRLink.PF_PWM_COMMAND_BACKWARD_7);
//					pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_CSTID, IRLink.PF_SOM_OUTPUT_B, IRLink.PF_CSTID_COMMAND_TOGGLEFULLBACKWARD);
                }
                // Wait some time
                Delay.msDelay(3000);
                for (int i = 0; i < 4; i++) {
                    // EM: Send toggle bit 1, next to 0. Will this work?
                    pfIRLink.sendPFExtended(i, NewIRLink.PF_EM_COMMAND_TOGGLEFORWARDFLOAT_OUTPUT_B);
                }

            }
        }
    }
}
