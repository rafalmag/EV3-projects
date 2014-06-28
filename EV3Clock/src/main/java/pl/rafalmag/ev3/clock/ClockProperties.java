package pl.rafalmag.ev3.clock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rafalmag.ev3.Time;

public class ClockProperties {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClockProperties.class);

	private static final String PROPERTIES_FILE_NAME = "clock.properties";

	private static final String TIME_HOUR = "timeHour";
	private static final String TIME_MINUTE = "timeMinute";

	private static class SubtitlesDownloaderPropertiesHolder {
		private static ClockProperties instance = new ClockProperties();
	}

	public static ClockProperties getInstance() {
		return SubtitlesDownloaderPropertiesHolder.instance;
	}

	private final Properties properties = new Properties();

	private ClockProperties() {
		load();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				store();
			}
		});
	}

	private synchronized void load() {
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
		} catch (IOException e) {
			LOGGER.debug(
					"Could not load properties, because of " + e.getMessage(),
					e);
		}
	}

	private synchronized void store() {
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE_NAME),
					"Clock properties");
		} catch (IOException e) {
			LOGGER.error(
					"Could not store properties, because of " + e.getMessage(),
					e);
		}
	}

	public synchronized Time getTime() {
		int hour = Integer.parseInt(properties.getProperty(TIME_HOUR, "12"));
		int minute = Integer.parseInt(properties.getProperty(TIME_MINUTE, "0"));
		return new Time(hour, minute);
	}

	public synchronized void setTime(Time time) {
		properties.setProperty(TIME_HOUR, "" + time.getHour());
		properties.setProperty(TIME_MINUTE, "" + time.getMinute());
	}
}
