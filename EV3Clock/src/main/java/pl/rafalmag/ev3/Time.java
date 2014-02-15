package pl.rafalmag.ev3;

import java.util.Calendar;
import java.util.Date;

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

	public Time(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		hour = calendar.get(Calendar.HOUR);
		minute = calendar.get(Calendar.MINUTE);
	}

	private static void validateTime(int hour, int minute) {
		Preconditions.checkArgument(hour >= 0 && hour <= 23,
				"%s must be in range [%s, %s]", hour, 0, 23);
		Preconditions.checkArgument(minute >= 0 && minute <= 59,
				"%s must be in range [%s, %s]", minute, 0, 59);
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d", hour, minute);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minute;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (hour != other.hour)
			return false;
		if (minute != other.minute)
			return false;
		return true;
	}

	public Time minusHour() {
		return new Time(getMinusHour(), minute);
	}

	private int getMinusHour() {
		int newHour = hour - 1;
		if (newHour < 0) {
			newHour = 23;
		}
		return newHour;
	}

	public Time plusHour() {
		return new Time(getPlusHour(), minute);
	}

	private int getPlusHour() {
		int newHour = hour + 1;
		if (newHour > 23) {
			newHour = 0;
		}
		return newHour;
	}

	public Time minusMinute() {
		int newMinute = minute - 1;
		int newHour = hour;
		if (newMinute < 0) {
			newMinute = 59;
			newHour = getMinusHour();
		}
		return new Time(newHour, newMinute);
	}

	public Time plusMinute() {
		int newMinute = minute + 1;
		int newHour = hour;
		if (newMinute > 59) {
			newMinute = 0;
			newHour = getPlusHour();
		}
		return new Time(newHour, newMinute);
	}

}
