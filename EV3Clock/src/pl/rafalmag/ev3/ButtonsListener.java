package pl.rafalmag.ev3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lejos.hardware.Button;

public class ButtonsListener implements Runnable {

	private static final int WAITFOR_RELEASE_SHIFT = 8;
	private final Map<Integer, Button> buttons;

	public ButtonsListener() {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(Button.ID_DOWN, Button.DOWN);
		buttons.put(Button.ID_ENTER, Button.ENTER);
		buttons.put(Button.ID_ESCAPE, Button.ESCAPE);
		buttons.put(Button.ID_LEFT, Button.LEFT);
		buttons.put(Button.ID_RIGHT, Button.RIGHT);
		buttons.put(Button.ID_UP, Button.UP);
		this.buttons = Collections.unmodifiableMap(buttons);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			int eventMask = Button.waitForAnyEvent();
			int pressedMask = eventMask & 0b11111111;
			int releasedMask = (eventMask & 0b111111100000000) >> WAITFOR_RELEASE_SHIFT;
			for (Entry<Integer, Button> entry : buttons.entrySet()) {
				if (isFlagSet(entry.getKey(), pressedMask)
						|| isFlagSet(entry.getKey(), releasedMask)) {
					entry.getValue().callListeners();
				}
			}
		}
	}

	private boolean isFlagSet(int flag, int eventMask) {
		return (eventMask & flag) == flag;
	}

}
