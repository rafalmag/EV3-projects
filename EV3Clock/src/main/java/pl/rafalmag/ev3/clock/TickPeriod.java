package pl.rafalmag.ev3.clock;

import java.util.concurrent.TimeUnit;

public class TickPeriod {

	private final long period;
	private final TimeUnit timeUnit;

	public TickPeriod(long period, TimeUnit timeUnit) {
		this.period = period;
		this.timeUnit = timeUnit;
	}

	public long getPeriod() {
		return period;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

}
