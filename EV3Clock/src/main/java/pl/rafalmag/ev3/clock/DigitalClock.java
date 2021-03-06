package pl.rafalmag.ev3.clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import lejos.hardware.lcd.LCD;
import pl.rafalmag.systemtime.SystemTime;

public class DigitalClock {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"HH:mm:ss");

	private final ClockRunning clockRunning;

	public DigitalClock(ClockRunning clockRunning) {
		this.clockRunning = clockRunning;
	}

	public void init() {
		clockRunning.addObserver(new ClockRunningService(new TickPeriod(1,
				TimeUnit.SECONDS)) {

			@Override
			public void onTick() {
				LCD.drawString(getTime(), 0, 5);
			}

			@Override
			public void onStart() {
				LCD.clear();
			}

			@Override
			public void onStop() {
				LCD.clear();
			}

		});
	}

	String getTime() {
		return DATE_FORMAT.format(SystemTime.getDate());
	}

}
