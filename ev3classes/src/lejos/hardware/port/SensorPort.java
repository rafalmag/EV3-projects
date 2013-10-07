package lejos.hardware.port;

import lejos.hardware.ev3.LocalEV3;

/**
 * Basic interface for EV3 sensor ports.
 * @author andy
 *
 */
public interface SensorPort {
    // TODO: The following do not really work with the new way of doing things
    // but there is code everywhere that uses this interface
    public static final Port S1 = LocalEV3.get().getPort("S1");
    public static final Port S2 = LocalEV3.get().getPort("S2");
    public static final Port S3 = LocalEV3.get().getPort("S3");
    public static final Port S4 = LocalEV3.get().getPort("S4");

}
