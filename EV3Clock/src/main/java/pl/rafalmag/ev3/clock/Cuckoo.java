package pl.rafalmag.ev3.clock;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lejos.hardware.Sound;
import lejos.robotics.RegulatedMotor;
import pl.rafalmag.ev3.AtomicWrappingCounter;
import pl.rafalmag.ev3.LoggingExceptionHandler;

public class Cuckoo {
	
	private static final Logger log = LoggerFactory.getLogger(Cuckoo.class);

	private static final int CUCKOO_ROTATION = 720;

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

	private final AtomicWrappingCounter tick = new AtomicWrappingCounter(0,
			AnalogClock.TICKS_PER_ROTATION);

	private static final String CUCKOO_WAV = "cuckoo.wav";

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
		try(InputStream is = getClass().getResourceAsStream(CUCKOO_WAV)){
			if(is ==null){
				throw new IOException("Cannot find wav="+CUCKOO_WAV);
			}
			int errorCode = Sound.playSample(is, Sound.VOL_MAX);
			if(errorCode <0){
				log.error("Cannot play cuckoo, error code="+errorCode);
			}
		} catch (IOException e) {
			log.error("Cannot play cuckoo, because of "+e.getMessage(),e);
		}
		
	}
	private void doCuckoo() {
		cuckooExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				playCuckoo();
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
