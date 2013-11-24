package pl.rafalmag.ev3.clock;

import lejos.hardware.Button;
import lejos.hardware.ButtonListener;
import pl.rafalmag.ev3.ButtonsListener;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public class ClockController {

	private final AnalogClock clock;

	public ClockController(AnalogClock clock) {
		this.clock = clock;
	}

	public void init() {
		addButtonsListeners();

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

		startButtonsListenerThread();
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
				System.out.println("Button.LEFT released");
				clock.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				System.out.println("Button.LEFT pressed");
				clock.fastBackward();
			}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				System.out.println("Button.RIGHT released");
				clock.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				System.out.println("Button.RIGHT pressed");
				clock.fastForward();
			}
		});

		Button.ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				System.out.println("Button.ENTER released");
				clock.toggleStart();
			}

			@Override
			public void buttonPressed(Button b) {
				System.out.println("Button.LEFT pressed");
				// wait for release
			}
		});

		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				clock.stop();
				Button.LEDPattern(0);
			}

			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub

			}
		});
	}
}
