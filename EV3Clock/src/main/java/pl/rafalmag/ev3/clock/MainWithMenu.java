package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.MirrorMotor;
import lejos.utility.TextMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.RuntimeInterruptedException;
import pl.rafalmag.ev3.Time;
import pl.rafalmag.systemtime.SystemTime;

public class MainWithMenu {

	private static final Logger log = LoggerFactory
			.getLogger(MainWithMenu.class);

	public static void main(String[] args) {
		log.info("Initializing...");
		LCD.clear();
		Button.setKeyClickVolume(1);
		SystemTime.initSysTime();
		Time initTime = new Time(12, 00);
		AnalogClock clock = new AnalogClock(initTime, new TickPeriod(1,
				TimeUnit.SECONDS),
				MirrorMotor
						.invertMotor(new EV3MediumRegulatedMotor(MotorPort.A)),
				new EV3LargeRegulatedMotor(MotorPort.B));
		MainWithMenu mainWithMenu = new MainWithMenu(clock);
		log.info("Ready");
		mainWithMenu.start();
	}

	private final TextMenu textMenu;
	private final AnalogClock clock;

	public MainWithMenu(AnalogClock clock) {
		this.clock = clock;
		String[] items = MainMenu.getNames();
		int topRow = 1;
		String title = "Clock";
		textMenu = new TextMenu(items, topRow, title);
	}

	public void start() {
		addRunningLeds();
		addShutdownHook();
		menuLoop();
	}

	private void addRunningLeds() {
		clock.getClockRunning().addObserver(new ClockRunningObserver() {

			@Override
			public void update(ClockRunning clockRunning, Boolean running) {
				if (running) {
					Button.LEDPattern(1); // green
				} else {
					Button.LEDPattern(0); // blank
				}
			}
		});
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				stopApp();
			}
		}, "Shutdown hook"));
	}

	public void menuLoop() {
		int lastSelected = 0;
		while (true) {
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected == -1) { // ESCAPE button
				stopApp();
				return;
			}
			MainMenu mainMenu = MainMenu.values()[lastSelected];
			switch (mainMenu) {
			case AUTO:
				// clock.setTime(); //TODO submenu ?
				clock.autoSet(SystemTime.getTime());
				break;
			case BACKWARD:
				clock.fastBackward();
				waitTillEnterIsDown();
				clock.stop();
				break;
			case FORWARD:
				clock.fastForward();
				waitTillEnterIsDown();
				clock.stop();
				break;
			case TOGGLE_RUN:
				clock.toggleStart();
				// TODO submenu with digital time ?
				break;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ mainMenu);
			}
		}
	}

	private void waitTillEnterIsDown() {
		while (Button.ENTER.isDown()) {
			try {
				Thread.sleep(10); // 10 ms
			} catch (InterruptedException e) {
				throw new RuntimeInterruptedException(e);
			}
		}
	}

	private void stopApp() {
		clock.stop();
		Button.LEDPattern(0);
		LCD.clear();
		LCD.drawString("Bye!", 0, 5);
		log.info("bye");
	}
}
