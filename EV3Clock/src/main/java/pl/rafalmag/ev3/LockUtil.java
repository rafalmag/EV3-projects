package pl.rafalmag.ev3;

import java.util.concurrent.locks.Lock;

public class LockUtil {

	public static void doInLock(Lock lock, Runnable action) {
		try {
			lock.lockInterruptibly();
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException(e);
		}
		try {
			action.run();
		} finally {
			lock.unlock();
		}

	}
}
