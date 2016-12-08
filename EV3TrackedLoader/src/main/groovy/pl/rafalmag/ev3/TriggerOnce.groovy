package pl.rafalmag.ev3

import ch.qos.logback.core.rolling.TriggeringPolicy

class TriggerOnce<E> implements TriggeringPolicy<E> {

	volatile boolean started
	volatile boolean triggered

	boolean isStarted() {
		return started
	}

	void start() {
		started = true
	}

	void stop() {
		started = false
	}

	 synchronized boolean isTriggeringEvent(File activeFile, E event) {
		if (triggered) {
			return false
		} else {
			triggered = true
			return true
		}
	}

}
