package pl.rafalmag.ev3;

import java.lang.Thread.UncaughtExceptionHandler;

public class LoggingExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(t.getName() + "thrown: ");
		e.printStackTrace();
	}

}
