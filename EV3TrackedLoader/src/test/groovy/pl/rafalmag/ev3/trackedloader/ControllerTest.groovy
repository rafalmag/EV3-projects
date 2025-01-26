package pl.rafalmag.ev3.trackedloader

import lejos.hardware.sensor.EV3IRSensor
import lejos.hardware.sensor.SensorMode
import lejos.robotics.RegulatedMotor
import lejos.robotics.navigation.DifferentialPilot
import spock.lang.Specification

// For java 17+ Run with: "--add-opens=java.base/java.lang=ALL-UNNAMED"
class ControllerTest extends Specification {

    def EV3IRSensor ev3IrSensor = Mock(EV3IRSensor)
    def DifferentialPilot differentialPilot = Mock(DifferentialPilot)
    def RegulatedMotor loaderMotor = Mock(RegulatedMotor)

    def Controller controller

    int channel = 0

    def setup() {
        ev3IrSensor.getSeekMode() >> Mock(SensorMode)
        controller = new Controller(ev3IrSensor, differentialPilot, loaderMotor, channel)
    }

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
