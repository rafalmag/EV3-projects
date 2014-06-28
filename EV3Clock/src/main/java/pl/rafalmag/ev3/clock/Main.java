package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.MirrorMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.Time;
import pl.rafalmag.systemtime.SystemTime;

@Deprecated
// TODO duplicated in MainWithMenu
public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		log.info("Initializing...");
		LCD.clear();
		LCD.drawString("Clock", 0, 5);

		SystemTime.initSysTime();

		ClockProperties clockProperties = ClockProperties.getInstance();
		AnalogClock clock = new AnalogClock(clockProperties, new TickPeriod(1,
				TimeUnit.SECONDS), new Time(0, 20),
				MirrorMotor
						.invertMotor(new EV3MediumRegulatedMotor(MotorPort.A)),
				new EV3LargeRegulatedMotor(MotorPort.B));
		ClockController clockController = new ClockController(clock);
		clockController.init();
		DigitalClock digitalClock = new DigitalClock(clock.getClockRunning());
		digitalClock.init();
		Button.setKeyClickVolume(1);
		log.info("Ready");
	}

}
