package pl.rafalmag.ev3.clock;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
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

	private static final int TICK_SPEED = 400;
	private static final int FAST_SPEED = 720;
	private static final float GEAR_RATIO = 1f / 6f;

	private final ClockRunning clockRunning = new ClockRunning(false);

	private final RegulatedMotor handMotor;
	private final Cuckoo cuckoo;

	private final AtomicReference<Time> time = new AtomicReference<>();

	private final AtomicInteger lastTachoCount = new AtomicInteger(0);

	private final Time tickTime;

	public AnalogClock(Time initTime, TickPeriod tickPeriod, Time tickTime,
			RegulatedMotor handMotor, RegulatedMotor cuckooMotor) {
		this.tickTime = tickTime;
		this.handMotor = handMotor;
		handMotor.resetTachoCount();
		handMotor.addListener(new RegulatedMotorListener() {

			@Override
			public void rotationStopped(RegulatedMotor motor, int tachoCount,
					boolean stalled, long timeStamp) {
				int lastTachoCount = AnalogClock.this.lastTachoCount
						.getAndSet(tachoCount);
				float angle = (tachoCount - lastTachoCount) * GEAR_RATIO;
				Time newTime = TimeAngleUtils.getTime(getTime(),
						Math.round(angle));
				setTime(newTime);
				log.debug("angle={}, time={}", angle, newTime);
			}

			@Override
			public void rotationStarted(RegulatedMotor motor, int tachoCount,
					boolean stalled, long timeStamp) {
			}
		});
		setTime(initTime); // TODO read from property file
		handMotor.setSpeed(TICK_SPEED);
		handMotor.setAcceleration(800);
		this.cuckoo = new Cuckoo(cuckooMotor, tickTime, time);

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
			}
		});
	}

	private void doTick() {
		handMotor.setSpeed(TICK_SPEED);
		handMotor.rotate(Math.round(TimeAngleUtils.toAngle(tickTime)
				/ GEAR_RATIO));
		doStop();
		cuckoo.checkCuckoo();
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

	public void autoSet(Date date) {
		Time timeToBeSet = new Time(date);
		Time baseTime = getTime();
		int diffAngle = Math.round(TimeAngleUtils.getDiffAngle(baseTime,
				timeToBeSet) / GEAR_RATIO);
		log.debug("baseTime={}, timeToBeSet={}, diffAngle={}", baseTime,
				timeToBeSet, diffAngle);
		handMotor.rotate(diffAngle);
		doStop();
	}

	public Time getTime() {
		log.debug("Getting time={}", time.get());
		return time.get();
	}

	public void setTime(Time time) {
		log.debug("Setting time={}", time);
		this.time.set(time);
		// TODO persist to property file ?
	}
}
