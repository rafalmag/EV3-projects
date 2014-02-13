package pl.rafalmag.ev3.clock;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClockRunning extends Observable {
	private final AtomicBoolean running;

	public ClockRunning(boolean running) {
		this.running = new AtomicBoolean(running);
	}

	public boolean isRunning() {
		return running.get();
	}

	public void setRunning(boolean newValue) {
		boolean oldValue = this.running.getAndSet(newValue);
		if (oldValue != newValue) {
			setChanged();
			notifyObservers(newValue);
		}
	}

	public boolean toggle() {
		boolean currentValue = negate(running);
		setChanged();
		notifyObservers(currentValue);
		return currentValue;
	}

	private static boolean negate(AtomicBoolean atomicBoolean) {
		boolean oldValue;
		do {
			oldValue = atomicBoolean.get();
		} while (!atomicBoolean.compareAndSet(oldValue, !oldValue));
		return !oldValue;
	}

}
