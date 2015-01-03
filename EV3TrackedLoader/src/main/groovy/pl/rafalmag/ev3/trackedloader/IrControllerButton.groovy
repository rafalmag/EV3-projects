package pl.rafalmag.ev3.trackedloader

/**
 * Maps codes based on {@link lejos.hardware.sensor.EV3IRSensor#getRemoteCommand(int)}
 */
enum IrControllerButton {
	NONE(0),
	TOP_LEFT(1),
	BOTTOM_LEFT(2),
	TOP_RIGHT(3),
	BOTTOM_RIGHT(4),
	TOP_LEFT_TOP_RIGHT(5),
	TOP_LEFT_BOTTOM_RIGHT(6),
	BOTTOM_LEFT_TOP_RIGHT(7),
	BOTTOM_LEFT_BOTTOM_RIGHT(8),
	CENTRE_BEACON(9),
	BOTTOM_LEFT_TOP_LEFT(10),
	TOP_RIGHT_BOTTOM_RIGHT(11)

	int code

	IrControllerButton(int code) {
		this.code = code
	}

	static IrControllerButton fromCode(int code) {
		IrControllerButton result = values().find {it.code == code}
		if(result == null){
			throw new IllegalArgumentException("Unsupported code: " + code)
		}
		result
	}

}
