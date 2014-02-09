package pl.rafalmag.ev3;

public class TimeAngleUtils {

	public static int toAngle(Time time) {
		return (time.getHour() % 12) * 360 + time.getMinute() * 360 / 60;
	}

	public static int getDiffAngle(Time baseTime, Time givenTime) {
		int baseTimeAngle = toAngle(baseTime);
		int givenTimeAngle = toAngle(givenTime);

		int diff = getDiffAbsolute(baseTimeAngle, givenTimeAngle);
		if (diff > 6 * 360) {
			return -(12 * 360 - diff);
		} else {
			return diff;
		}
	}

	private static int getDiffAbsolute(int baseTimeAngle, int givenTimeAngle) {
		if (baseTimeAngle < givenTimeAngle) {
			return givenTimeAngle - baseTimeAngle;
		} else if (givenTimeAngle < baseTimeAngle) {
			return -(baseTimeAngle - givenTimeAngle);
		} else {
			// equal
			return 0;
		}
	}

}
