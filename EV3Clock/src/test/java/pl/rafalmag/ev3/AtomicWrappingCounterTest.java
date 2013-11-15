package pl.rafalmag.ev3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AtomicWrappingCounterTest {

	@Test
	public void should_increment_mod3() {
		// given
		AtomicWrappingCounter atomicWrappingCounter = new AtomicWrappingCounter(
				0, 3);

		// when + then
		assertEquals(atomicWrappingCounter.incrementAndGet(), 1);
		assertEquals(atomicWrappingCounter.incrementAndGet(), 2);
		assertEquals(atomicWrappingCounter.incrementAndGet(), 0);
		assertEquals(atomicWrappingCounter.incrementAndGet(), 1);
	}
}
