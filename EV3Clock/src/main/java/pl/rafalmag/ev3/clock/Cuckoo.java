package pl.rafalmag.ev3.clock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.LoggingExceptionHandler;
import pl.rafalmag.ev3.RuntimeInterruptedException;
import pl.rafalmag.ev3.Time;

import com.google.common.io.Resources;

public class Cuckoo {

	private static final int EXTRA_WAIT_TIME_MS = 100;

	private static final Logger log = LoggerFactory.getLogger(Cuckoo.class);

	private static final int CUCKOO_ROTATION = 90;
	private static final int CUCKOO_SPEED = 300;

	private final ScheduledExecutorService cuckooExecutor = Executors
			.newScheduledThreadPool(2, new ThreadFactory() {

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

	public static final String CUCKOO_WAV = "cuckoo.wav";

	private final Time tickTime;

	private final ClockProperties clockProperties;

	static {
		copyResource();
	}

	private static void copyResource() {
		URL url = Cuckoo.class.getClass().getResource("/" + CUCKOO_WAV);
		try {
			File file = new File(CUCKOO_WAV);
			if (file.exists()) {
				log.info("File " + file.getAbsolutePath()
						+ " already exists - so it won't be overriden");
			} else {
				try (OutputStream os = new FileOutputStream(file, false)) {
					Resources.copy(url, os);
				}
			}
		} catch (IOException e) {
			log.error("Cannot extract " + CUCKOO_WAV + " file");
		}
	}

	public Cuckoo(RegulatedMotor cuckooMotor, Time tickTime,
			ClockProperties clockProperties) {
		this.cuckooMotor = cuckooMotor;
		this.tickTime = tickTime;
		this.clockProperties = clockProperties;
		cuckooMotor.setAcceleration(400);
		cuckooMotor.setSpeed(CUCKOO_SPEED);
	}

	public void checkCuckoo() {
		cuckooExecutor.execute(new Runnable() {

			@Override
			public void run() {
				if (shouldCuckoo(clockProperties.getTime(), tickTime)) {
					doCuckoo();
				}
			}

		});
	}

	static boolean shouldCuckoo(Time analogTime, Time tickTime) {
		int positiveBound = tickTime.getMinute() / 2;
		int negativeBound = 60 - positiveBound;
		int minutesOnClock = analogTime.getMinute();
		return minutesOnClock == 0 || minutesOnClock >= negativeBound
				|| minutesOnClock < positiveBound;
	}

	private void doCuckoo() {
		// first cuckoo
		rotateAndCuckoo();
		// second cuckoo
		rotateAndCuckoo();
	}

	private void rotateAndCuckoo() {
		cuckooMotor.rotate(CUCKOO_ROTATION);
		cuckooMotor.stop();
		cuckooMotor.flt();
		sleep(EXTRA_WAIT_TIME_MS);
		int playTimeMs = playCuckoo();
		if (playTimeMs < 0) {
			log.error("Cannot play cuckoo, error code=" + playTimeMs);
		} else {
			// ok
			sleep(playTimeMs + EXTRA_WAIT_TIME_MS);
		}
		cuckooMotor.rotate((int) (-CUCKOO_ROTATION * 0.9));
		cuckooMotor.stop();
		cuckooMotor.flt();
	}

	private void sleep(long sleepTimeMs) {
		try {
			Thread.sleep(sleepTimeMs);
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException(e);
		}
	}

	private int playCuckoo() {
		return Sound.playSample(new File(CUCKOO_WAV), Sound.VOL_MAX);
	}

}
