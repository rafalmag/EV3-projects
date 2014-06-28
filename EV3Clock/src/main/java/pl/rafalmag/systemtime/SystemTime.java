package pl.rafalmag.systemtime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemTime {

	private static final Logger log = LoggerFactory.getLogger(SystemTime.class);

	private static final TimeZone TIME_ZONE = TimeZone
			.getTimeZone("Europe/Warsaw");

	private static final AtomicLong offsetMs = new AtomicLong(0);

	public static void setOffset(long offsetMs) {
		SystemTime.offsetMs.set(offsetMs);
	}

	public static Date getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MILLISECOND, (int) offsetMs.get());
		calendar.add(Calendar.MILLISECOND, TIME_ZONE.getRawOffset());
		calendar.add(Calendar.MILLISECOND, TIME_ZONE.getDSTSavings());
		return calendar.getTime();
	}

	private static String NTP_SERVER = "pl.pool.ntp.org";

	public static void initSysTime() {
		initSysTime(NTP_SERVER);
	}

	public static void initSysTime(String ntpServer) {
		SystemTimeManager systemTimeManager = new SystemTimeManager(ntpServer);
		try {
			long offsetMs = systemTimeManager.getOffsetMs();
			SystemTime.setOffset(offsetMs);
		} catch (SystemTimeManagerException e) {
			log.error(
					"Could not adjust system clock, because of "
							+ e.getMessage(), e);
		}
	}

}
