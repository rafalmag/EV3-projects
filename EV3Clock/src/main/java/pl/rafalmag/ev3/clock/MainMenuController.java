package pl.rafalmag.ev3.clock;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.TextMenu;
import pl.rafalmag.ev3.Ev3Utils;
import pl.rafalmag.systemtime.SystemTime;

public class MainMenuController {

	private static final int TIMEOUT_MS = 50;
	private final AnalogClock clock;
	private final ClockSettingMenuController clockSettingMenuController;

	public MainMenuController(AnalogClock clock,
			ClockSettingMenuController clockSettingMenuController) {
		this.clock = clock;
		this.clockSettingMenuController = clockSettingMenuController;
	}

	public void mainMenuLoop() {
		LCD.drawString("Clock setting", 0, 0);
		TextMenu textMenu = new TextMenu(MainMenu.getNames(), 2);
		int lastSelected = 0;
		while (true) {
			textMenu.setTitle(clock.getTime().toString());
			// blocking here
			lastSelected = textMenu.select(lastSelected);
			if (lastSelected < 0) { // esc
				return;
			}
			MainMenu mainMenu = MainMenu.values()[lastSelected];
			switch (mainMenu) {
			case AUTO:
				clock.autoSet(SystemTime.getDate());
				break;
			case BACKWARD:
				clock.fastBackward();
				Ev3Utils.waitTillEnterIsDown();
				clock.stop();
				break;
			case FORWARD:
				clock.fastForward();
				Ev3Utils.waitTillEnterIsDown();
				clock.stop();
				break;
			case TOGGLE_RUN:
				clock.toggleStart();
				displayTime();
				break;
			case HAND_SETTINGS:
				LCD.clear();
				clockSettingMenuController.clockSettingMenu();
				LCD.clear();
				break;
			default:
				throw new IllegalStateException("Not supported enum value = "
						+ mainMenu);
			}
		}
	}

	/**
	 * Displays time and waits for any button to be pressed.
	 */
	private void displayTime() {
		TextLCD timeLCD = BrickFinder.getDefault().getTextLCD(
				Font.getLargeFont());
		LCD.clear();
		do {
			Ev3Utils.displayLargeText(timeLCD, clock.getTime().toString());
		} while (Button.waitForAnyPress(TIMEOUT_MS) == 0);
		timeLCD.clear();
	}

}
