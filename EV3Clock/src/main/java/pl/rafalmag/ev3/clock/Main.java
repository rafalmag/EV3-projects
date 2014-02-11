package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.MirrorMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.Time;
import pl.rafalmag.systemtime.SystemTime;
import pl.rafalmag.systemtime.SystemTimeManager;
import pl.rafalmag.systemtime.SystemTimeManagerException;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static String NTP_SERVER = "pl.pool.ntp.org";

	public static void main(String[] args) throws InterruptedException {
		log.info("Initializing...");
		LCD.clear();
		LCD.drawString("Clock", 0, 5);

		initSysTime();

		Time initTime = new Time(12, 00);
		AnalogClock clock = new AnalogClock(initTime, new TickPeriod(1, TimeUnit.SECONDS), MirrorMotor.invertMotor(Motor.A), Motor.B);
		ClockController clockController = new ClockController(clock);
		clockController.init();
		DigitalClock digitalClock = new DigitalClock(clock.getClockRunning());
		digitalClock.init();
		Button.setKeyClickVolume(1);
		log.info("Ready");
	}

	private static void initSysTime() {
		SystemTimeManager systemTimeManager = new SystemTimeManager(NTP_SERVER);
		try {
			long offsetMs = systemTimeManager.getOffsetMs();
			SystemTime.setOffset(offsetMs);
		} catch (SystemTimeManagerException e) {
			log.error("Could not adjust system clock, because of " + e.getMessage(), e);
		}
	}

}
