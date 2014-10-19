package pl.rafalmag.ev3.trackedloader

import groovy.util.logging.Slf4j
import lejos.hardware.Button
import lejos.hardware.Key
import lejos.hardware.KeyListener
import lejos.hardware.sensor.EV3IRSensor
import lejos.robotics.RegulatedMotor
import lejos.robotics.navigation.DifferentialPilot

import static pl.rafalmag.ev3.trackedloader.IrControllerButton.*

@Slf4j
class Controller {

    private final EV3IRSensor ev3IrSensor
    private final DifferentialPilot differentialPilot
    private final RegulatedMotor loaderMotor
    private final int channel
    private SeekSensor seekSensor = new SeekSensor(ev3IrSensor.getSeekMode())
    private DistanceSensor distanceSensor = new DistanceSensor(ev3IrSensor.getDistanceMode())

    public Controller(EV3IRSensor ev3IrSensor, DifferentialPilot differentialPilot, RegulatedMotor loaderMotor, int channel) {
        this.ev3IrSensor = ev3IrSensor
        this.differentialPilot = differentialPilot
        this.loaderMotor = loaderMotor
        this.channel = channel
    }

    public void doJob() {
        Button.ESCAPE.addKeyListener(new KeyListener() {
            @Override
            void keyPressed(Key key) {
                log.info("Bye!")
            }

            @Override
            void keyReleased(Key key) {
                loaderMotor.flt()
                differentialPilot.stop()
                System.exit(0)
            }
        })
        while (true) {
            int remoteCommand = ev3IrSensor.getRemoteCommand(channel)
            handleIrCommand(remoteCommand)
        }
    }

    void handleIrCommand(int remoteCommand) {
        IrControllerButton irControllerButton = IrControllerButton.fromCode(remoteCommand)
        def differentialPilotCommand = { differentialPilot.stop() }
        def loaderMotorCommand = { loaderMotor.flt() }
        String command;
        switch (irControllerButton) {
            case NONE:
                // default closures
                command = "idle..."
                break
            case TOP_LEFT:
                command = "turn right forward"
                differentialPilotCommand = { differentialPilot.steer(-100) }
                break
            case BOTTOM_LEFT:
                command = "turn right backward"
                differentialPilotCommand = { differentialPilot.steerBackward(-100) }
                break
            case TOP_RIGHT:
                command = "turn left forward"
                differentialPilotCommand = { differentialPilot.steer(100) }
                break
            case BOTTOM_RIGHT:
                command = "turn left backward"
                differentialPilotCommand = { differentialPilot.steerBackward(100) }
                break
            case TOP_LEFT_TOP_RIGHT:
                command = "forward"
                differentialPilotCommand = { differentialPilot.forward() }
                break
            case TOP_LEFT_BOTTOM_RIGHT:
                command = "turn right in place"
                differentialPilotCommand = { differentialPilot.steer(-200) }
                break
            case BOTTOM_LEFT_TOP_RIGHT:
                command = "turn left in place"
                differentialPilotCommand = { differentialPilot.steer(200) }
                break
            case BOTTOM_LEFT_BOTTOM_RIGHT:
                command = "backward"
                differentialPilotCommand = { differentialPilot.backward() }
                break
            case BOTTOM_LEFT_TOP_LEFT:
                command = "bucket down"
                loaderMotorCommand = { loaderMotor.backward() }
                break
            case TOP_RIGHT_BOTTOM_RIGHT:
                command = "bucket up"
                loaderMotorCommand = { loaderMotor.forward() }
                break
            case CENTRE_BEACON:
                autoMode()
                return
            default:
                throw new IllegalStateException("Unhandled controller type: " + irControllerButton)
        }
        log.info("Command $command");
        // TODO draw string
//        LCD.drawString(command, 0, 0);

        differentialPilotCommand.call()
        loaderMotorCommand.call()
    }

    def autoMode() {
        log.info("auto mode")
        // more info http://www.ev-3.net/en/archives/848
        def beaconOptional = seekSensor.getSeekSamples().getClosestBeacon()
        if (beaconOptional.present) {
            def beacon = beaconOptional.get()
            if (beacon.distanceCm > 20) {
                differentialPilot.steer(beacon.bearing)
//            differentialPilot.travel(5) // 5cm // TODO travel forward ?
            } else {
                log.info("Got you !")
                // TODO bucket part
            }
        }
    }
}

