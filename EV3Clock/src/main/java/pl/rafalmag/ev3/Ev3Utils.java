package pl.rafalmag.ev3;

import lejos.hardware.Button;
import lejos.hardware.lcd.TextLCD;

public class Ev3Utils {

	private static final int TIMEOUT_MS = 50;

	public static void displayLargeText(TextLCD textLCD, String text) {
		textLCD.clear();
		textLCD.drawString(text,
				(textLCD.getTextWidth() - text.length() + 1) / 2, 1);
	}

	public static void waitTillEnterIsDown() {
		while (Button.ENTER.isDown()) {
			TimeUtil.sleep(TIMEOUT_MS);
		}
	}
}
