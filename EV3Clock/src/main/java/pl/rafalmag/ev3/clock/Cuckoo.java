package pl.rafalmag.ev3.clock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.LoggingExceptionHandler;
import pl.rafalmag.ev3.Time;

import com.google.common.io.Resources;

public class Cuckoo {

	private static final Logger log = LoggerFactory.getLogger(Cuckoo.class);

	private static final int CUCKOO_ROTATION = 720;
	private static final int CUCKOO_SPEED = 300;

	private final Executor cuckooExecutor = Executors
			.newCachedThreadPool(new ThreadFactory() {

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

	private final AtomicReference<Time> analogTime;

	private final Time tickTime;

	static {
		copyResource();
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

	public Cuckoo(RegulatedMotor cuckooMotor, Time tickTime,
			AtomicReference<Time> analogTime) {
		this.cuckooMotor = cuckooMotor;
		this.tickTime = tickTime;
		this.analogTime = analogTime;
		cuckooMotor.setAcceleration(400);
		cuckooMotor.setSpeed(CUCKOO_SPEED);
	}

	public void checkCuckoo() {
		cuckooExecutor.execute(new Runnable() {

			@Override
			public void run() {
				if (shouldCuckoo(analogTime.get(), tickTime)) {
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

	private void playCuckoo() {
		int errorCode = Sound.playSample(new File(CUCKOO_WAV), Sound.VOL_MAX);
		if (errorCode < 0) {
			log.error("Cannot play cuckoo, error code=" + errorCode); // TODO
																		// -5
																		// returned
		}
	}

	private void doCuckoo() {
		playCuckoo();
		cuckooMotor.rotate(CUCKOO_ROTATION);
		cuckooMotor.stop();
		cuckooMotor.flt();
	}

}
