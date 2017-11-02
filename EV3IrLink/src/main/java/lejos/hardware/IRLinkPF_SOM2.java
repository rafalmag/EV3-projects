package lejos.hardware;

import lejos.hardware.device.NewIRLink;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

/**
 * Description: LeJOS NXJ new IRLink driver.
 * Run PF Single Output Mode Clear/Set/Toggle/Inc/Dec submode Tests.
 */

public class IRLinkPF_SOM2 {

    /* Sensors */
    private NewIRLink pfIRLink;

    /* Constructor */
    public IRLinkPF_SOM2() {
        pfIRLink = new NewIRLink(SensorPort.S2);
    }

    public static void main(String[] args) {
        IRLinkPF_SOM2 obj = new IRLinkPF_SOM2();
        obj.testPFSingleOutputMode_SubmodeCSTID(); // Run PF Single Output Mode Clear/Set/Toggle/Inc/Dec submode Tests.
    }

    // PF Single Output Mode Clear/Set/Toggle/Inc/Dec submode Tests.
    public void testPFSingleOutputMode_SubmodeCSTID() {
        LCD.drawString("PF SOM CSTID:", 0, 0);
        while (!(Button.ESCAPE.isDown())) {

            if (Button.ENTER.isDown()) {
                // CSTID: Toggle Full Forward or Full Backward
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_A, NewIRLink.PF_CSTID_COMMAND_TOGGLEFULLFORWARD);
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_B, NewIRLink.PF_CSTID_COMMAND_TOGGLEFULLFORWARD);
                    LCD.drawString("Toggle FF  ", 0, 1);
                    //pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_CSTID, IRLink.PF_SOM_OUTPUT_A, IRLink.PF_CSTID_COMMAND_TOGGLEFULLBACKWARD);
                    //pfIRLink.sendPFSingleOutput(i, IRLink.PF_SOM_SUBMODE_CSTID, IRLink.PF_SOM_OUTPUT_B, IRLink.PF_CSTID_COMMAND_TOGGLEFULLBACKWARD);
                    //LCD.drawString("Toggle FB  ", 0, 1);

                    // Wait some time between each channel.
                    Delay.msDelay(100);
                }
            }

            if (Button.RIGHT.isDown()) {
                // CSTID: Increase speed on all motors.
                LCD.drawString("Incr PWM   ", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_A, NewIRLink.PF_CSTID_COMMAND_INCREMENT_NUMERICAL_PWM);
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_B, NewIRLink.PF_CSTID_COMMAND_INCREMENT_NUMERICAL_PWM);

                    // Wait some time between each channel.
                    Delay.msDelay(100);
                }
            }

            if (Button.LEFT.isDown()) {
                // CSTID: Decrease speed on all motors.
                LCD.drawString("Decr PWM  ", 0, 1);
                for (int i = 0; i < 4; i++) {
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_A, NewIRLink.PF_CSTID_COMMAND_DECREMENT_NUMERICAL_PWM);
                    pfIRLink.sendPFSingleOutput(i, NewIRLink.PF_SOM_SUBMODE_CSTID, NewIRLink.PF_SOM_OUTPUT_B, NewIRLink.PF_CSTID_COMMAND_DECREMENT_NUMERICAL_PWM);

                    // Wait some time between each channel.
                    Delay.msDelay(100);
                }
            }
        }
    }
}
