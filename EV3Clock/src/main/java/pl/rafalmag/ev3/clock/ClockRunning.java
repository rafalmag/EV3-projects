package pl.rafalmag.ev3.clock;

import java.util.Observable;

public class ClockRunning extends Observable {
	private boolean running;

	public ClockRunning(boolean running) {
		this.running = running;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
		setChanged();
		notifyObservers(running);
	}

	public synchronized boolean toggle() {
		running = !running;
		setChanged();
		notifyObservers(running);
		return running;
	}

}
