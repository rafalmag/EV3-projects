package lejos.nxt;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * An abstraction for a sensor port that supports 
 * setting and retrieving types and modes of sensors.
 * 
 * @author Lawrie Griffiths.
 *
 */
public interface BasicSensorPort extends SensorConstants {

    public int getMode();
	
    @Deprecated
    public int getType();
	
    public boolean setMode(int mode);
	
    @Deprecated
    public boolean setType(int type);
	
    @Deprecated
    public boolean setTypeAndMode(int type, int mode);

}

