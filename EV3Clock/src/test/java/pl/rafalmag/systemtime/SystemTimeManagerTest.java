package pl.rafalmag.systemtime;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pl.rafalmag.systemtime.SystemTimeManager;

public class SystemTimeManagerTest {

	@Test
	public void should_get_offset() throws Exception {
		// given
		String ntpServer = "pl.pool.ntp.org";
		SystemTimeManager systemTimeManager = new SystemTimeManager(ntpServer);
		// when
		Long offsetMs = systemTimeManager.getOffsetMs();
		// then
		assertThat(offsetMs, notNullValue());
	}

}