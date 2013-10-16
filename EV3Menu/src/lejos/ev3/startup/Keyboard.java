package lejos.ev3.startup;
import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.ev3.LocalEV3;

public class Keyboard {
	
	
	int x = 0, y = 5;
	
	String[] lower = {"0123456789-=_+    ", 
			          ".,@:;?/()*!\"£$%^&#", 
			          "qwertyuiop[]{}    ", 
			          "asdfghjkl         ", 
			          "zxcvbnm<>|\\`¬     ", 
			          "U l 0 x D         "};
	
	String[] upper = {"0123456789-=_+    ", 
	                  ".,@:;?/()*!\"£$%^&#", 
	                  "QWERTYUIOP[]{}    ", 
	                  "ASDFGHJKL         ", 
	                  "ZXCVBNM<>|\\`¬     ", 
	                  "U l x D           "};
	
	String[] lines = lower;
	
	void display() {
		//LCD.drawString("Keyboard", 4, 0);
		LCD.clearDisplay();
		for(int i=0;i<lines.length;i++) {
			LCD.drawString(lines[i], 0, i+1);
		}
		displayCursor(true);
	}
	
	void displayCursor(boolean inverted) {
		LCD.drawString(lines[y-1].substring(x,x+1), x, y, inverted);
	}
	
	String getString() {
		StringBuilder sb = new StringBuilder();
		x = 0;
		y = 5;
		display();
		
		while (true) {
			int b = Button.waitForAnyPress();
			
			displayCursor(false);
			
			if (b == Button.ID_DOWN) {
				if (++y > 6) y = 1;
			} else if (b == Button.ID_UP) {
				if (--y < 1) y = 6;
			} else if (b == Button.ID_LEFT) {
				if (--x < 0) x = 17;
			} else if (b == Button.ID_RIGHT) {
				if (++x > 17) x = 0;
			} else if (b == Button.ID_ENTER) {
				if (y < 6) sb.append(lines[y-1].charAt(x));
				else {
					switch (lines[5].charAt(x)) {
					case 'U': 
						lines = upper;
						display();
					    break;
					case 'l':
						lines = lower;
						display();
						break;
					case 'x':
						sb.deleteCharAt(sb.length()-1);
						break;
					case 'D':
						return sb.toString();
					}
				}
			} else if (b == Button.ID_ESCAPE) {
				return null;
			}
			
			displayCursor(true);
			LCD.drawString(sb.toString(), 0, 7);
		}
	}
	
	public static void main(String[] args) {
		Keyboard k = new Keyboard();
		
		String s = k.getString();
		System.out.println("String is " + s);
	}
}
