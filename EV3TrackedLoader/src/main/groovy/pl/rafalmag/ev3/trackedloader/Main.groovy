package pl.rafalmag.ev3.trackedloader

import groovy.util.logging.Slf4j
import lejos.hardware.Button
import lejos.hardware.Key
import lejos.hardware.KeyListener
import lejos.hardware.motor.EV3LargeRegulatedMotor
import lejos.hardware.motor.EV3MediumRegulatedMotor
import lejos.hardware.port.MotorPort
import lejos.hardware.port.SensorPort
import lejos.hardware.sensor.EV3IRSensor
import lejos.robotics.MirrorMotor
import lejos.robotics.navigation.DifferentialPilot

@Slf4j
class Main {

    static final CHANNEL = 0
    static final TRACK_WIDTH = 12.8 // cm
    static final WHEEL_DIAMETER = 4.8 //cm
    static final IR_SENSOR_PORT = SensorPort.S2
    static final LEFT_MOTOR = MirrorMotor.invertMotor(new EV3LargeRegulatedMotor(MotorPort.B))
    static final RIGHT_MOTOR = MirrorMotor.invertMotor(new EV3LargeRegulatedMotor(MotorPort.D))
    static final LOADER_MOTOR = new EV3MediumRegulatedMotor(MotorPort.C)
    static final EV3_IR_SENSOR = new EV3IRSensor(IR_SENSOR_PORT)

    public static void main(String[] args) {
        log.info("Initializing...")
//		LCD.clear()
//		disableOtherLogLayers()
//		LCD.clear()
        Button.setKeyClickVolume(1)
        try {
            LOADER_MOTOR.setSpeed(LOADER_MOTOR.getMaxSpeed())

            def differentialPilot = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, LEFT_MOTOR, RIGHT_MOTOR)
            differentialPilot.setTravelSpeed(differentialPilot.getMaxTravelSpeed() * 0.5)
            addKeyListener(differentialPilot)

            def controller = new Controller(EV3_IR_SENSOR, differentialPilot, LOADER_MOTOR, CHANNEL)
            controller.doJob()
        } finally {
            EV3_IR_SENSOR?.close()
            LOADER_MOTOR?.close()
            LEFT_MOTOR?.close()
            RIGHT_MOTOR?.close()
        }
    }

    private static addKeyListener(differentialPilot) {
        Button.ENTER.addKeyListener(new KeyListener() {
            @Override
            void keyPressed(Key key) {
                log.info("Stop all!")
            }

            @Override
            void keyReleased(Key key) {
                differentialPilot.stop()
            }
        })
        Button.ESCAPE.addKeyListener(new KeyListener() {
            @Override
            void keyPressed(Key key) {
                log.info("Stop all!")
            }

            @Override
            void keyReleased(Key key) {
                EV3_IR_SENSOR?.close()
                LOADER_MOTOR?.close()
                LEFT_MOTOR?.close()
                RIGHT_MOTOR?.close()
                differentialPilot.stop()
                System.exit(0)
            }
        })
    }

//    def static disableOtherLogLayers() {
//        // similar to lejos.internal.ev3.EV3Wrapper.switchToLayer(String)
//        for (EV3LCDManager.LCDLayer layer : EV3LCDManager.getLocalLCDManager().getLayers()) {
//            if (!layer.getName().equalsIgnoreCase("LCD")) {
//                layer.setVisible(false)
//            }
//        }
//    }
}
