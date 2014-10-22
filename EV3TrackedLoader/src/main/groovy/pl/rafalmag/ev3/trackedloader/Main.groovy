package pl.rafalmag.ev3.trackedloader

import groovy.util.logging.Slf4j
import lejos.hardware.Button
import lejos.hardware.Key
import lejos.hardware.KeyListener
import lejos.hardware.motor.Motor
import lejos.hardware.port.SensorPort
import lejos.hardware.sensor.EV3IRSensor
import lejos.internal.ev3.EV3LCDManager
import lejos.robotics.navigation.DifferentialPilot

@Slf4j
class Main {

    static final CHANNEL = 0
    static final TRACK_WIDTH = 12.8 // cm
    static final WHEEL_DIAMETER = 4.8 //cm
    static final IR_SENSOR_PORT = SensorPort.S2

    static final LEFT_MOTOR = Motor.B
    static final RIGHT_MOTOR = Motor.D
    static final LOADER_MOTOR = Motor.C

    public static void main(String[] args) {
        log.info("Initializing...")
//		LCD.clear()
//		disableOtherLogLayers()
//		LCD.clear()
        Button.setKeyClickVolume(1)
        EV3IRSensor ev3IRSensor = null
        try {
            ev3IRSensor = new EV3IRSensor(IR_SENSOR_PORT)
            def differentialPilot = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, LEFT_MOTOR, RIGHT_MOTOR)

            Button.ESCAPE.addKeyListener(new KeyListener() {
                @Override
                void keyPressed(Key key) {
                    log.info("Stop all!")
                }

                @Override
                void keyReleased(Key key) {
//                ev3IRSensor?.close()
//                LOADER_MOTOR?.close()
//                LEFT_MOTOR?.close()
//                RIGHT_MOTOR?.close()
//                ev3IrSensor.close()
                    differentialPilot.stop()
//                System.exit(0)
                }
            })

            def controller = new Controller(ev3IRSensor, differentialPilot, LOADER_MOTOR, CHANNEL)
            controller.doJob()
        } finally {
            ev3IRSensor?.close()
            LOADER_MOTOR?.close()
            LEFT_MOTOR?.close()
            RIGHT_MOTOR?.close()
        }
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
