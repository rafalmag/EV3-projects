package pl.rafalmag.systemtime;

import java.util.concurrent.atomic.AtomicLong;

public class SystemTime {

	private final static AtomicLong offsetMs = new AtomicLong(0);

	public static void setOffset(long offsetMs) {
		SystemTime.offsetMs.set(offsetMs);
	}

	public static long getTime() {
		return System.currentTimeMillis() + offsetMs.get();
	}
}
