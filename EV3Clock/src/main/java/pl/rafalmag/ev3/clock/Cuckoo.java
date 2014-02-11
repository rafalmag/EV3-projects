package pl.rafalmag.ev3.clock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.AtomicWrappingCounter;
import pl.rafalmag.ev3.LoggingExceptionHandler;

import com.google.common.io.Resources;

public class Cuckoo {

	private static final Logger log = LoggerFactory.getLogger(Cuckoo.class);

	private static final int CUCKOO_ROTATION = 720;

	private final Executor cuckooExecutor = Executors.newCachedThreadPool(new ThreadFactory() {

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

	private final AtomicWrappingCounter tick = new AtomicWrappingCounter(0, AnalogClock.TICKS_PER_ROTATION);

	public static final String CUCKOO_WAV = "cuckoo.rsf";

	static {
		// TODO temporary switched off
		// copyResource();
	}

	private static void copyResource() {
		URL url = Cuckoo.class.getClass().getResource("/" + CUCKOO_WAV);
		try {
			File file = new File(CUCKOO_WAV);
			try (OutputStream os = new FileOutputStream(file, false)) {
				Resources.copy(url, os);
			}
		} catch (IOException e) {
			log.error("Cannot extract " + CUCKOO_WAV + " file");
		}
	}

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

	private void playCuckoo() {
		int errorCode = Sound.playSample(new File(CUCKOO_WAV), Sound.VOL_MAX);
		if (errorCode < 0) {
			log.error("Cannot play cuckoo, error code=" + errorCode); // TOTO
																		// -5
																		// returned
		}
	}

	private void doCuckoo() {
		cuckooExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// playCuckoo(); // TODO temporary switched off
			}

		});
		cuckooMotor.rotate(CUCKOO_ROTATION);
		cuckooMotor.stop();
		cuckooMotor.flt();
	}

	public void resetTickCount() {
		tick.reset();
	}
}
