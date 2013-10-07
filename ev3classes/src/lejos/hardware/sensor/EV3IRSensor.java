package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.RangeFinder;

/**
 * Basic sensor driver for the Lego EV3 Infra Red sensor.<br>
 * TODO: Need to implement other modes. Consider adding support for some of the
 * Range* interfaces.
 * @author andy
 *
 */
public class EV3IRSensor extends UARTSensor implements RangeFinder
{
    protected final static int IR_PROX = 0;
    protected final static int IR_SEEK = 1;
    protected final static int IR_REMOTE = 2;
    
    protected final static int SWITCH_DELAY = 250;
    
    public final static int IR_CHANNELS = 4;
    
    protected byte [] remoteVals = new byte[IR_CHANNELS];

    public EV3IRSensor(UARTPort port)
    {
        super(port, IR_PROX);
    }
    
    public EV3IRSensor(Port port)
    {
        super(port, IR_PROX);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getRange()
    {
        switchMode(IR_PROX, SWITCH_DELAY);
        return (int)port.getByte() & 0xff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[] getRanges()
    {
        float [] result = new float[1];
        result[0] = getRange();
        return result;
    }

    /**
     * Return the current remote command from the specified channel. Remote commands
     * are a single numeric value  which represents which button on the Lego IR
     * remote is currently pressed (0 means no buttons pressed). Four channels are
     * supported (0-3) which correspond to 1-4 on the remote. The button values are:<br>
     * 1 TOP-LEFT<br>
     * 2 BOTTOM-LEFT<br>
     * 3 TOP-RIGHT<br>
     * 4 BOTTOM-RIGHT<br>
     * 5 TOP-LEFT + TOP-RIGHT<br>
     * 6 TOP-LEFT + BOTTOM-RIGHT<br>
     * 7 BOTTOM-LEFT + TOP-RIGHT<br>
     * 8 BOTTOM-LEFT + BOTTOM-RIGHT<br>
     * 9 CENTRE/BEACON<br>
     * 10 BOTTOM-LEFT + TOP-LEFT<br>
     * 11 TOP-RIGHT + BOTTOM-RIGHT<br>
     * @param chan channel to obtain the command for
     * @return the current command
     */
    public int getRemoteCommand(int chan)
    {
        switchMode(IR_REMOTE, SWITCH_DELAY);
        port.getBytes(remoteVals, 0, remoteVals.length);
        return remoteVals[chan] & 0xff;
    }

    /**
     * Obtain the commands associated with one or more channels. Each element of 
     * the array contains the command for the associated channel (0-3).
     * @param cmds the array to store the commands
     * @param offset the offset to start storing
     * @param len the number of commands to store.
     */
    public void getRemoteCommands(byte[] cmds, int offset, int len)
    {
        switchMode(IR_REMOTE, SWITCH_DELAY);
        port.getBytes(cmds, offset, len);
    }

    /**
     * return the bearing and distance to one or more IR beacons. Up to four
     * beacons (on different channels 0-3) can be detected. Each beacon has an
     * associated two byte value (so the beacon on channel 0 will have values
     * in locations 0 and 1 in the array. The first location contains the relative
     * bearing to the beacon, the second the distance.<br>
     * The bearing values range from -12 to +12 (with values increasing clockwise
     * when looking from behind the sensor. A bearing of 0 indicates the beacon is
     * directly in front of the sensor. Distance values (0-100) are in cm and if no
     * beacon is detected a bearing of 0 and a distance of -128(255) is returned.
     * @param bearings
     * @param offset
     * @param len
     */
    public void getBearings(byte[] bearings, int offset, int len)
    {
        switchMode(IR_SEEK, SWITCH_DELAY);
        port.getBytes(bearings, offset, len);
    }

}
