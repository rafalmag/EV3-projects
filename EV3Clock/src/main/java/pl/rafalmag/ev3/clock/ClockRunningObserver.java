package pl.rafalmag.ev3.clock;

import java.util.Observable;
import java.util.Observer;

public abstract class ClockRunningObserver implements Observer {
	@Override
	public void update(Observable observable, Object newValue) {
		update((ClockRunning) observable, (Boolean) newValue);
	}

	public abstract void update(ClockRunning clockRunning, Boolean running);
}
