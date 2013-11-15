package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.MirrorMotor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Main started");
		LCD.clear();
		LCD.drawString("Clock", 0, 5);

		Clock clock = new Clock(new TickPeriod(1, TimeUnit.SECONDS),
				MirrorMotor.invertMotor(Motor.A), Motor.B);
		ClockController clockController = new ClockController(clock);
		clockController.init();
		Button.setKeyClickVolume(1);
		System.out.println("Main ready");
		// buttons listener is not a deamon, so application won't stop
		// waitForever();
	}

	// private static void waitForever() throws InterruptedException {
	// while (true) {
	// Thread.sleep(1000);
	// }
	// }

}
