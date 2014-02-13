package pl.rafalmag.ev3.clock;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.Time;
import pl.rafalmag.ev3.TimeAngleUtils;

public class AnalogClock {

	private static final Logger log = LoggerFactory
			.getLogger(AnalogClock.class);

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

	private final AtomicReference<Time> time = new AtomicReference<>();

	public AnalogClock(final Time initTime, TickPeriod tickPeriod,
			RegulatedMotor handMotor, RegulatedMotor cuckooMotor) {
		this.handMotor = handMotor;
		handMotor.resetTachoCount();
		handMotor.addListener(new RegulatedMotorListener() {

			@Override
			public void rotationStopped(RegulatedMotor motor, int tachoCount,
					boolean stalled, long timeStamp) {
				double angle = tachoCount * GEAR_RATIO;
				Time newTime = TimeAngleUtils.getTime(initTime, (int) angle);
				time.set(newTime);
				log.debug("angle ={}, time={}", angle, newTime);
			}

			@Override
			public void rotationStarted(RegulatedMotor motor, int tachoCount,
					boolean stalled, long timeStamp) {
			}
		});
		time.set(initTime);
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
		doStop();
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

	public void autoSet(long date) {
		Time timeToBeSet = new Time(new Date(date));
		int diffAngle = TimeAngleUtils.getDiffAngle(time.get(), timeToBeSet);
		handMotor.rotate((int) (diffAngle / GEAR_RATIO));
		doStop();
	}
}
