package pl.rafalmag.ev3.clock;

import lejos.hardware.Button;
import lejos.hardware.ButtonListener;
import lejos.hardware.lcd.LCD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.ButtonsListener;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public class ClockController {

	private static final Logger log = LoggerFactory
			.getLogger(AnalogClock.class);

	private final AnalogClock clock;

	public ClockController(AnalogClock clock) {
		this.clock = clock;
	}

	public void init() {
		addButtonsListeners();
		startButtonsListenerThread();
		addRunningLeds();
		addShutdownHook();
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
				LCD.clear();
				LCD.drawString("Bye!", 0, 5);

			}
		}, "Shutdown hook"));
	}

	private void startButtonsListenerThread() {
		Thread thread = new Thread(new ButtonsListener(), "Buttons Listener");
		thread.setDaemon(false);
		thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
		thread.start();
	}

	private void addButtonsListeners() {
		Button.LEFT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				log.trace("Button.LEFT released");
				clock.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				log.trace("Button.LEFT pressed");
				clock.fastBackward();
			}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				log.trace("Button.RIGHT released");
				clock.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				log.trace("Button.RIGHT pressed");
				clock.fastForward();
			}
		});

		Button.ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				log.trace("Button.ENTER released");
				clock.toggleStart();
			}

			@Override
			public void buttonPressed(Button b) {
				log.trace("Button.ENTER pressed");
				// wait for release
			}
		});

		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				log.trace("Button.ESCAPE released");
				stopApp();
			}

			@Override
			public void buttonPressed(Button b) {
				log.trace("Button.ESCAPE pressed");
			}
		});
	}

	private void stopApp() {
		clock.stop();
		Button.LEDPattern(0);
	}
}
