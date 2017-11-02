package lejos.hardware;

import lejos.hardware.device.NewIRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;

/**
 * Description: LeJOS NXJ new IRLink driver.
 * Run PF Combo Direct Mode Tests.
 */

public class IRLinkPF_CDM {

    /* Sensors */
    private NewIRLink pfIRLink;

    /* Constructor */
    public IRLinkPF_CDM() {
        pfIRLink = new NewIRLink(SensorPort.S2);
    }

    public static void main(String[] args) {
        IRLinkPF_CDM obj = new IRLinkPF_CDM();
        obj.testPFComboDirectMode(); // Run PF Combo Direct Mode Tests.
    }

    // PF Combo Direct Mode Tests.
    public void testPFComboDirectMode() {

        LCD.drawString("PF CDM:", 0, 0);
        while (!(Button.ESCAPE.isDown())) {

            if (Button.ENTER.isDown()) {
                // Stop all motors on output A and output B.
                LCD.drawString("Fl A, Fl B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFComboDirect(i, NewIRLink.PF_CDM_COMMAND_BRAKE, NewIRLink.PF_CDM_COMMAND_BRAKE);
                }
            }

            if (Button.RIGHT.isDown()) {
                // Full forward motors on output A and full backward motors on output B.
                LCD.drawString("FF A, FB B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFComboDirect(i, NewIRLink.PF_CDM_COMMAND_FORWARD, NewIRLink.PF_CDM_COMMAND_BACKWARD);
                }
            }

            if (Button.LEFT.isDown()) {
                // Full backward motors on output A and full forwardard motors on output B.
                LCD.drawString("FB A, FF B", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFComboDirect(i, NewIRLink.PF_CDM_COMMAND_BACKWARD, NewIRLink.PF_CDM_COMMAND_FORWARD);
                }
            }
        }
    }
}
