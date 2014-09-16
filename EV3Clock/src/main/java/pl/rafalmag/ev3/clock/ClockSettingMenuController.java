package pl.rafalmag.ev3.clock;

import lejos.hardware.lcd.LCD;
import lejos.utility.TextMenu;
import pl.rafalmag.ev3.Time;

public class ClockSettingMenuController {
	private final AnalogClock clock;

	public ClockSettingMenuController(AnalogClock clock) {
		this.clock = clock;
	}

	public void clockSettingMenu() {
		Time time = clock.getTime();
		LCD.drawString("Clock setting", 0, 0);
		TextMenu textMenu = new TextMenu(ClockSettingMenu.getNames(), 2);
		int lastSelected = 0;
		while (true) {
			textMenu.setTitle(time.toString());
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected < 0) { // esc
				clock.setTime(time);
				return;
			}
			ClockSettingMenu clockSettingMenu = ClockSettingMenu.values()[lastSelected];
			switch (clockSettingMenu) {
			case HOUR_MINUS:
				time = time.minusHour();
				break;
			case HOUR_PLUS:
				time = time.plusHour();
				break;
			case MINUTE_MINUS:
				time = time.minusMinute();
				break;
			case MINUTE_PLUS:
				time = time.plusMinute();
				break;
			case OK:
				clock.setTime(time);
				return;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ clockSettingMenu);
			}
		}
	}
}