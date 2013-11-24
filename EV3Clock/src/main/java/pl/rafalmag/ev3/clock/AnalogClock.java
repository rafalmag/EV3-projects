package pl.rafalmag.ev3.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lejos.robotics.RegulatedMotor;
import pl.rafalmag.ev3.LockUtil;
import pl.rafalmag.ev3.LoggingExceptionHandler;

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
	private Cuckoo cuckoo;

	public AnalogClock(final TickPeriod tickPeriod, final RegulatedMotor handMotor,
			RegulatedMotor cuckooMotor) {
		this.handMotor = handMotor;
		handMotor.setSpeed(TICK_SPEED);
		handMotor.setAcceleration(800);
		this.cuckoo = new Cuckoo(cuckooMotor);
		cuckooMotor.setAcceleration(400);
		cuckooMotor.setSpeed(CUCKOO_SPEED);

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
					start(tickPeriod);
				} else {
					stop();
				}
			}

			private void stop() {
				LockUtil.doInLock(lock, new Runnable() {

					@Override
					public void run() {
						executor.shutdownNow();
					}
				});
				doStop();
			}

			private void start(final TickPeriod tickPeriod) {
				LockUtil.doInLock(lock, new Runnable() {

					@Override
					public void run() {
						if (executor.isShutdown()) {
							executor = getNewExecutor();
							executor.scheduleAtFixedRate(new Runnable() {

								@Override
								public void run() {
									doTick();
								}

							}, 0, tickPeriod.getPeriod(),
									tickPeriod.getTimeUnit());
							cuckoo.resetTickCount();
						} else {
							System.out.println("Clock is already running");
						}

					}
				});
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
