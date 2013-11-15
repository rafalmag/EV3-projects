package pl.rafalmag.ev3.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lejos.robotics.RegulatedMotor;
import pl.rafalmag.ev3.LockUtil;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public class Clock {

	private static final int TICK_SPEED = 400;
	private static final int FAST_SPEED = 720;
	private static final float GEAR_RATIO = 1f / 6f;
	private static final int TICK_ANGLE = Math.round(new Float(
			1f / 12f * 360f / GEAR_RATIO));

	private final ClockRunning clockRunning = new ClockRunning(false);

	private final RegulatedMotor motor;

	public Clock(final TickPeriod tickPeriod, final RegulatedMotor motor) {
		this.motor = motor;
		motor.setSpeed(TICK_SPEED);
		motor.setAcceleration(800);

		clockRunning.addObserver(new ClockRunningObserver() {

			private final Lock lock = new ReentrantLock();
			// guarded by lock
			private ScheduledExecutorService executor = getNewExecutor();

			{
				executor.shutdown();
			}

			private ScheduledExecutorService getNewExecutor() {
				return Executors
						.newSingleThreadScheduledExecutor(new ThreadFactory() {

							@Override
							public Thread newThread(Runnable runnable) {
								Thread thread = new Thread(runnable);
								thread.setDaemon(true);
								thread.setName("Scheduled tick");
								thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
								return thread;
							}
						});
			}

			@Override
			public void update(ClockRunning clockRunning, Boolean running) {
				System.out.println("Clock running=" + running);
				if (running) {
					start(tickPeriod, motor);
				} else {
					stop(motor);
				}
			}

			private void stop(final RegulatedMotor motor) {
				LockUtil.doInLock(lock, new Runnable() {

					@Override
					public void run() {
						executor.shutdownNow();
					}
				});
				motor.stop();
				motor.flt();
			}

			private void start(final TickPeriod tickPeriod,
					final RegulatedMotor motor) {
				LockUtil.doInLock(lock, new Runnable() {

					@Override
					public void run() {
						if (executor.isShutdown()) {
							executor = getNewExecutor();
							executor.scheduleAtFixedRate(new Runnable() {

								@Override
								public void run() {
									motor.setSpeed(TICK_SPEED);
									motor.rotate(TICK_ANGLE);
									motor.stop();
									motor.flt();
								}
							}, 0, tickPeriod.getPeriod(),
									tickPeriod.getTimeUnit());
						} else {

						}

					}
				});
			}
		});
	}

	public void start() {
		clockRunning.setRunning(true);
	}

	public void stop() {
		clockRunning.setRunning(false);
	}

	public void fastForward() {
		stop();
		motor.setSpeed(FAST_SPEED);
		motor.forward();
	}

	public void fastBackward() {
		stop();
		motor.setSpeed(FAST_SPEED);
		motor.backward();
	}

	public void toggleStart() {
		clockRunning.toggle();
	}

	public ClockRunning getClockRunning() {
		return clockRunning;
	}
}
