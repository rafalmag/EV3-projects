package pl.rafalmag.ev3.trackedloader

import lejos.hardware.sensor.EV3IRSensor
import lejos.robotics.RegulatedMotor
import lejos.robotics.navigation.DifferentialPilot
import spock.lang.Specification

class ControllerTest extends Specification {

    def EV3IRSensor ev3IrSensor = Mock(EV3IRSensor)
    def DifferentialPilot differentialPilot = Mock(DifferentialPilot)
    def RegulatedMotor loaderMotor = Mock(RegulatedMotor)

    def Controller controller = new Controller(ev3IrSensor, differentialPilot, loaderMotor, channel)
    int channel = 0

    def "should stop"() {
        when:
        controller.handleIrCommand(0);

        then:
        1 * differentialPilot.stop()
        1 * loaderMotor.flt()
    }

    def "should go forward"() {
        when:
        controller.handleIrCommand(IrControllerButton.TOP_LEFT_TOP_RIGHT.getCode())

        then:
        1 * differentialPilot.forward()
    }
}
