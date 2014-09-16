package pl.rafalmag.ev3;

public class TimeUtil {

	public static void sleep(long sleepTimeMs) {
		try {
			Thread.sleep(sleepTimeMs);
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException(e);
		}
	}

}
