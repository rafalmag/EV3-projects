package pl.rafalmag.ev3;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicy;

public class TriggerOnce<E> implements TriggeringPolicy<E> {

	volatile boolean started;
	volatile boolean triggered = false;

	public boolean isStarted() {
		return started;
	}

	public void start() {
		started = true;
	}

	public void stop() {
		started = false;
	}

	public synchronized boolean isTriggeringEvent(File arg0, E arg1) {
		if (triggered) {
			return false;
		} else {
			triggered = true;
			return true;
		}
	}

}
