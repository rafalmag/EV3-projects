package pl.rafalmag.ev3;

import com.google.common.base.Preconditions;

/**
 * @author Rafal
 * 
 */
public class Time {

	private final int hour;
	private final int minute;

	public Time(int hour, int minute) {
		validateTime(hour, minute);
		this.hour = hour;
		this.minute = minute;
	}

	private static void validateTime(int hour, int minute) {
		Preconditions.checkArgument(hour >= 0 && hour <= 24, "%s must be in range [%s, %s]", hour, 0, 24);
		Preconditions.checkArgument(minute >= 0 && minute <= 59, "%s must be in range [%s, %s]", minute, 0, 59);
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	@Override
	public String toString() {
		return String.format("Time=%02d:%02d", hour, minute);
	}

}
