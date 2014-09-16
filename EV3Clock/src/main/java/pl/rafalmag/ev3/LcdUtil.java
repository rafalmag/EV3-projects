package pl.rafalmag.ev3;

import lejos.hardware.lcd.TextLCD;

public class LcdUtil {

	public static void displayLargeText(TextLCD textLCD, String text) {
		textLCD.clear();
		textLCD.drawString(text,
				(textLCD.getTextWidth() - text.length() + 1) / 2, 1);
	}
}
