package pl.rafalmag.ev3;

public class AtomicWrappingCounter {
	private int value;
	private final int max;

	public AtomicWrappingCounter(int init, int max) {
		this.value = init;
		this.max = max;
	}

	public synchronized int get() {
		return value;
	}

	public synchronized int incrementAndGet() {
		value = (value + 1) % max;
		return value;
	}

	public void reset() {
		value = 0;
	}
}