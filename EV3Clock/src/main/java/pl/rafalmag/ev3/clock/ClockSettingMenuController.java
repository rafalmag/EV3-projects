package pl.rafalmag.ev3.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.TextMenu;
import pl.rafalmag.ev3.Ev3Utils;
import pl.rafalmag.ev3.RuntimeInterruptedException;

public class ClockSettingMenuController {
	private final AnalogClock clock;

	public ClockSettingMenuController(AnalogClock clock) {
		this.clock = clock;
	}

	public void clockSettingMenu() {
		LCD.drawString("Clock setting", 0, 0);
		TextMenu textMenu = new TextMenu(ClockSettingMenu.getNames(), 2);
		int lastSelected = 0;
		while (true) {
			textMenu.setTitle(clock.getTime().toString());
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected < 0) { // esc
				return;
			}
			ClockSettingMenu clockSettingMenu = ClockSettingMenu.values()[lastSelected];
			switch (clockSettingMenu) {
			case HOUR_MINUS:
				updateClockWhileEnterIsDown(new Runnable() {

					@Override
					public void run() {
						clock.setTime(clock.getTime().minusHour());
					}
				});
				break;
			case HOUR_PLUS:
				updateClockWhileEnterIsDown(new Runnable() {

					@Override
					public void run() {
						clock.setTime(clock.getTime().plusHour());
					}
				});
				break;
			case MINUTE_MINUS:
				updateClockWhileEnterIsDown(new Runnable() {

					@Override
					public void run() {
						clock.setTime(clock.getTime().minusMinute());
					}
				});
				break;
			case MINUTE_PLUS:
				updateClockWhileEnterIsDown(new Runnable() {

					@Override
					public void run() {
						clock.setTime(clock.getTime().plusMinute());
					}
				});
				break;
			case OK:
				return;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ clockSettingMenu);
			}
		}
	}

	private void updateClockWhileEnterIsDown(final Runnable action) {
		LCD.clear();
		final TextLCD timeLCD = BrickFinder.getDefault().getTextLCD(
				Font.getLargeFont());
		final ScheduledExecutorService executor = Executors
				.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				action.run();
				timeLCD.clear();
				Ev3Utils.displayLargeText(timeLCD, clock.getTime().toString());

			}
		}, 0, 500, TimeUnit.MILLISECONDS);
		Ev3Utils.waitTillEnterIsDown();
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException(e);
		}
		timeLCD.clear();
	}
}