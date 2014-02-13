package pl.rafalmag;

public class CordsToAngle {

	public static double getAngleForFxCoordinates(double x, double y) {
		return getAngleForCoordinates(x, -y);
	}

	public static double getAngleForCoordinates(double x, double y) {
		double radians = Math.atan2(y, x);
		double degrees = radians * 180 / Math.PI;

		if (x >= 0 && y > 0) {
			return 360 - degrees;
		} else if (x > 0 && y <= 0) {
			return -degrees;
		} else if (x < 0 && y >= 0) {
			return 360 - degrees;
		} else if (x <= 0 && y <= 0) {
			return -degrees;
		} else {
			throw new IllegalStateException("this should be handled, degrees ="
					+ degrees);
		}

	}

}
