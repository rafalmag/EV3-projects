package pl.rafalmag.ev3.clock;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lejos.robotics.RegulatedMotor;
import pl.rafalmag.ev3.AtomicWrappingCounter;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public class Cuckoo {

	private static final int CUCKOO_ROTATION = 720;

	private final Executor cuckooExecutor = Executors
			.newSingleThreadExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable runnable) {
					Thread thread = new Thread(runnable);
					thread.setDaemon(true);
					thread.setName("Cuckoo");
					thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
					return thread;
				}
			});

	private final RegulatedMotor cuckooMotor;

	private final AtomicWrappingCounter tick = new AtomicWrappingCounter(0,
			Clock.TICKS_PER_ROTATION);

	public Cuckoo(RegulatedMotor cuckooMotor) {
		this.cuckooMotor = cuckooMotor;
	}

	public void cuckoo() {
		cuckooExecutor.execute(new Runnable() {

			@Override
			public void run() {
				if (tick.incrementAndGet() == 0) {
					doCuckoo();
				}
			}

		});
	}

	private void doCuckoo() {
		cuckooMotor.rotate(CUCKOO_ROTATION);
		cuckooMotor.stop();
		cuckooMotor.flt();
	}

	public void resetTickCount() {
		tick.reset();
	}
}
