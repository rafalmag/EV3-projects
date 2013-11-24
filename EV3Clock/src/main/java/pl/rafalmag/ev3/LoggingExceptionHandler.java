package pl.rafalmag.ev3;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger log = LoggerFactory
			.getLogger(LoggingExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error(t.getName() + " with exception: " + e.getMessage(), e);
	}

}
