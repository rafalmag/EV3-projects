package pl.rafalmag.ev3;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class JulConfigure {

    private static final String PROPERTIES_FILE = "Ev3clockLogging.properties";

    /**
     * Must be called before any logger is used.
     */
    public static void configureJul() {
        URL resource = Resources.getResource("logging.properties");
        if (resource != null) {
            try (InputStream is = resource.openStream()) {
                Files.copy(is, Paths.get(PROPERTIES_FILE), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.setProperty("java.util.logging.config.file", PROPERTIES_FILE);
        }
    }
}
