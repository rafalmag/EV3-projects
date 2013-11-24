package pl.rafalmag.ev3.clock;

import lejos.robotics.RegulatedMotor;

public class AnalogClock {

	private static final int CUCKOO_SPEED = 300;
	private static final int TICK_SPEED = 400;
	private static final int FAST_SPEED = 720;
	private static final float GEAR_RATIO = 1f / 6f;
	static final int TICKS_PER_ROTATION = 12;
	private static final int TICK_ANGLE = Math.round(new Float(1f
			/ TICKS_PER_ROTATION * 360f / GEAR_RATIO));

	private final ClockRunning clockRunning = new ClockRunning(false);

	private final RegulatedMotor handMotor;
	private final Cuckoo cuckoo;

	public AnalogClock(final TickPeriod tickPeriod,
			final RegulatedMotor handMotor, RegulatedMotor cuckooMotor) {
		this.handMotor = handMotor;
		handMotor.setSpeed(TICK_SPEED);
		handMotor.setAcceleration(800);
		this.cuckoo = new Cuckoo(cuckooMotor);
		cuckooMotor.setAcceleration(400);
		cuckooMotor.setSpeed(CUCKOO_SPEED);

		clockRunning.addObserver(new ClockRunningService(tickPeriod) {

			@Override
			public void onTick() {
				doTick();
			}

			@Override
			public void onStop() {
				doStop();
			}

			@Override
			public void onStart() {
				cuckoo.resetTickCount();
			}
		});
	}

	private void doTick() {
		handMotor.setSpeed(TICK_SPEED);
		handMotor.rotate(TICK_ANGLE);
		doStop();
		cuckoo.cuckoo();
	}

	private void doStop() {
		handMotor.stop();
		handMotor.flt();
	}

	public void start() {
		clockRunning.setRunning(true);
	}

	public void stop() {
		clockRunning.setRunning(false);
	}

	public void fastForward() {
		stop();
		handMotor.setSpeed(FAST_SPEED);
		handMotor.forward();
	}

	public void fastBackward() {
		stop();
		handMotor.setSpeed(FAST_SPEED);
		handMotor.backward();
	}

	public void toggleStart() {
		clockRunning.toggle();
	}

	public ClockRunning getClockRunning() {
		return clockRunning;
	}
}
