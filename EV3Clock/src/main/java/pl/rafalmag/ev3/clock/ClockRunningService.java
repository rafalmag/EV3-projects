package pl.rafalmag.ev3.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.LockUtil;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public abstract class ClockRunningService extends ClockRunningObserver {

	private static final Logger log = LoggerFactory.getLogger(ClockRunningService.class);

	private final Lock lock = new ReentrantLock();
	// guarded by lock
	private ScheduledExecutorService executor = getNewExecutor();

	private final TickPeriod tickPeriod;

	public ClockRunningService(TickPeriod tickPeriod) {
		executor.shutdown();
		this.tickPeriod = tickPeriod;
	}

	private ScheduledExecutorService getNewExecutor() {
		return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

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
		log.debug("Clock running={}", running);
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
				// shutdownNow - hung the application at hang at lejos.internal.ev3.EV3MotorPort.EV3MotorRegulatorKernelModule.subMove()
				// http://www.lejos.org/forum/viewtopic.php?f=18&t=5822&p=22608#p22608
				executor.shutdown();
			}
		});
		onStop();
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
							onTick();
						}

					}, 0, tickPeriod.getPeriod(), tickPeriod.getTimeUnit());
					onStart();
				} else {
					log.debug("Clock is already running");
				}

			}
		});
	}

	public abstract void onTick();

	public abstract void onStart();

	public abstract void onStop();

}
