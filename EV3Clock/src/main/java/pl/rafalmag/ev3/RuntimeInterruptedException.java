package pl.rafalmag.ev3;

@SuppressWarnings("serial")
public class RuntimeInterruptedException extends RuntimeException {

	/**
	 * Interrupts the thread and throws e as runtime exception.
	 * 
	 * @param e
	 */
	public RuntimeInterruptedException(InterruptedException e) {
		super(e);
		Thread.currentThread().interrupt();
	}
}
