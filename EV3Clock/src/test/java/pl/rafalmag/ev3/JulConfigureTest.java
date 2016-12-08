package pl.rafalmag.ev3;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JulConfigureTest {

    static {
        JulConfigure.configureJul();
    }

    private static final Logger log = LoggerFactory.getLogger(JulConfigureTest.class);

    // to be manually verified after test run
    @Ignore
    @Test
    public void shouldLog() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }

}
