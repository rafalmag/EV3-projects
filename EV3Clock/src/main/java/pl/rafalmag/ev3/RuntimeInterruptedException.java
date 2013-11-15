package pl.rafalmag.ev3;

@SuppressWarnings("serial")
public class RuntimeInterruptedException extends RuntimeException {

	public RuntimeInterruptedException(InterruptedException e) {
		super(e);
	}
}
