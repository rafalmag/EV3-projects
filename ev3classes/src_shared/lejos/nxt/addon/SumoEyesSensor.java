package lejos.nxt.addon;

import lejos.nxt.AnalogPort;
import lejos.nxt.AnalogSensor;
import lejos.nxt.Port;
import lejos.nxt.SensorConstants;

/**
 * Java class for MINDSENSORS NXT SumoEyes (triple zone IR obstacle detector).
 *
 * @author Daniele Benedettelli
 * @version 1.0
 */
public class SumoEyesSensor extends AnalogSensor implements SensorConstants {

	
    /** The Constant NO_DETECTION (0). */
    public final static int NO_DETECTION = 0;
    
    /** The Constant LEFT (1). */
    public final static int LEFT = 1;
    
    /** The Constant CENTER (2). */
    public final static int CENTER = 2;
    
    /** The Constant RIGHT (3). */
    public final static int RIGHT = 3;
        
    /** The long range. */
    private boolean longRange = false;

    /**
     * Default constructor.
     *
     * @param port the sensor port
     */
    public SumoEyesSensor(AnalogPort port) {
        this(port, false);
    }

    /**
     * Constructor with range specification.
     *
     * @param port the sensor port
     * @param longRange if true, enables long range
     */
    public SumoEyesSensor(AnalogPort port, boolean longRange) {
        super(port);
        this.longRange = longRange;
        port.setTypeAndMode(TYPE_LIGHT_ACTIVE,MODE_PCTFULLSCALE);
    }

    /**
     * Default constructor.
     *
     * @param port the sensor port
     */
    public SumoEyesSensor(Port port) {
        this(port, false);
    }

    /**
     * Constructor with range specification.
     *
     * @param port the sensor port
     * @param longRange if true, enables long range
     */
    public SumoEyesSensor(Port port, boolean longRange) {
        super(port);
        setLongRange(longRange);
    }

    /**
     * Gets the raw value of the sensor.
     *
     * @return the raw sensor value
     */
    public int getValue() {
        return port.readRawValue();
    }

    /**
     * Returns the detected zone (NO_DETECTION (0) , RIGHT (1), CENTER (2), LEFT (3))
     * @return detected zone constant
     *
     */
    public int getObstacle() {
        int value = port.readRawValue();
        if (value == 1023) {
            return NO_DETECTION;
        }
        if (value > 300 && value < 400) {
            return CENTER;
        }
        if (value > 600) {
            return LEFT;
        }
        return RIGHT;
    }

    /**
     * Enables long range of the sensor.
     *
     * @param longRange if true, enables long range, if false enables short range
     */
    public void setLongRange(boolean longRange) {
        this.longRange = longRange;
        if (this.longRange) {
        	port.setTypeAndMode(TYPE_LIGHT_ACTIVE, MODE_PCTFULLSCALE);
        } else
        	port.setTypeAndMode(TYPE_LIGHT_INACTIVE, MODE_PCTFULLSCALE);
    }

    /**
     * Returns the current range of the sensor.
     *
     * @return current range of the sensor
     */
    public boolean isLongRange() {
        return longRange;
    }
}
