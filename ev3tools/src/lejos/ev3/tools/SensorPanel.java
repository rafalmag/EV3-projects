package lejos.ev3.tools;

import java.awt.Dimension;
import javax.swing.*;

/**
 * JPanel that displays two gauges: one for the raw and
 * one for the scaled value of a NXT sensor.
 *
 * @author Lawrie Griffiths
 */
public class SensorPanel extends JPanel {
	private static final long serialVersionUID = 3592127880184905255L;
	private static final int DEFAULT_MAX_RAW_VALUE = 1023;
	private static final int DEFAULT_MAX_SCALED_VALUE = 100;
	private static final Dimension PANEL_SIZE = new Dimension(120,350);
	
	private LabeledGauge gauge;
	private JLabel nameLabel, typeLabel = new JLabel("No Sensor");
	
	/**
	 * Create the panel
	 * 
	 * @param name the name of the sensor
	 */
	public SensorPanel(String name) {
		nameLabel = new JLabel(name);
		gauge = new LabeledGauge("Scaled", DEFAULT_MAX_SCALED_VALUE);
		
		add(nameLabel);
		add(gauge);		
		add(typeLabel);

		Dimension size = PANEL_SIZE;
		setSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Set the scaled value of the sensor
	 * 
	 * @param val the scaled value
	 */
	public void setScaledVal(int val) {
		gauge.setVal(val);
	}
	
	/**
	 * Set the maximum scaled value for the sensor
	 * 
	 * @param val the maximum scaled value
	 */
	public void setScaledMaxVal(int val) {
		gauge.setMaxVal(val);
	}
	
	/**
	 * Set the type of the sensor
	 * 
	 * @param type the sensor type
	 */
	public void setType(String type) {
		typeLabel.setText(type);
	}
}
