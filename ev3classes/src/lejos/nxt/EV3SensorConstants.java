package lejos.nxt;

/**
 * Basic constants for use when accessing EV3 Sensors.
 * @author andy
 *
 */
public interface EV3SensorConstants
{
    public static final int PORTS = 4;
    
    public static final int CONN_UNKNOWN    = 111;  //!< Connection is fake (test)
    public static final int CONN_DAISYCHAIN = 117;  //!< Connection is daisy chained
    public static final int CONN_NXT_COLOR  = 118;  //!< Connection type is NXT color sensor
    public static final int CONN_NXT_DUMB   = 119;  //!< Connection type is NXT analog sensor
    public static final int CONN_NXT_IIC    = 120;  //!< Connection type is NXT IIC sensor
    public static final int CONN_INPUT_DUMB = 121;  //!< Connection type is LMS2012 input device with ID resistor
    public static final int CONN_INPUT_UART = 122;  //!< Connection type is LMS2012 UART sensor
    public static final int CONN_OUTPUT_DUMB= 123;  //!< Connection type is LMS2012 output device with ID resistor^
    public static final int CONN_OUTPUT_INTELLIGENT= 124;  //!< Connection type is LMS2012 output device with communication
    public static final int CONN_OUTPUT_TACHO= 125;  //!< Connection type is LMS2012 tacho motor with series ID resistance
    public static final int CONN_NONE       = 126;  //!< Port empty or not available
    public static final int CONN_ERROR      = 127;  //!< Port not empty and type is invalid^M
    
    public static final int UART_MAX_MODES = 8;
}
