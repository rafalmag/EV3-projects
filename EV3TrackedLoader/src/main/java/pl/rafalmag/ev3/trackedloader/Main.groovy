package pl.rafalmag.ev3.trackedloader

import groovy.util.logging.Slf4j
import lejos.hardware.Button
import lejos.hardware.lcd.LCD
import lejos.hardware.motor.Motor
import lejos.hardware.motor.NXTRegulatedMotor
import lejos.hardware.port.Port
import lejos.hardware.port.SensorPort
import lejos.hardware.sensor.EV3IRSensor
import lejos.internal.ev3.EV3LCDManager
import lejos.robotics.RegulatedMotor
import lejos.robotics.navigation.DifferentialPilot
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Slf4j
class Main {

	static final CHANNEL = 0
	static final TRACK_WIDTH = 20
	static final WHEEL_DIAMETER = 20
	static final IR_SENSOR_PORT = SensorPort.S1
	static final LEFT_MOTOR = Motor.B
	static final RIGHT_MOTOR = Motor.C
	static final LOADER_MOTOR = Motor.A

	public static void main(String[] args) {
		log.info("Initializing...")
//		LCD.clear()
//		disableOtherLogLayers()
//		LCD.clear()
		Button.setKeyClickVolume(1)

		def ev3IRSensor = new EV3IRSensor(IR_SENSOR_PORT)
		def differentialPilot = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, LEFT_MOTOR, RIGHT_MOTOR)
		def controller = new Controller(ev3IRSensor, differentialPilot, LOADER_MOTOR, CHANNEL)
		controller.doJob()
	}

	def static disableOtherLogLayers() {
		// similar to lejos.internal.ev3.EV3Wrapper.switchToLayer(String)
		for (EV3LCDManager.LCDLayer layer : EV3LCDManager.getLocalLCDManager().getLayers()) {
			if (!layer.getName().equalsIgnoreCase("LCD")) {
				layer.setVisible(false)
			}
		}
	}
}
