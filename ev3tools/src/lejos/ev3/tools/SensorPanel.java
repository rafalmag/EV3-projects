package lejos.ev3.tools;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RemoteEV3;

public class SensorPanel extends JPanel implements ItemListener {
	private static final long serialVersionUID = -4569637982551819105L;
	
	private static final String[] sensorTypes = { "No Sensor", "EV3 Touch",
		"EV3 IR", "EV3 Color", "EV3 Ultrasonic", "EV3 Gyro",
		"NXT Touch", "NXT Sound", "NXT Light", "NXT Color", "NXT Ultrasonic",
		"HiTechnic Gyro", "HiTechnic Acceleration", "HiTechnic Color",
		"HiTechnic Compass", "HiTechnic Barometric", "HiTechnic Angle",
		"HiTechnic Magnetic","HiTechnic IRSeeker", "HiTechnic EOPD",
		"Mindsensors Acceleration", "Mindsensors Compass", "Mindsensors Distance",
		"Mindsensors LineLeader", "Mindsensors Pressure",
		"Dexter IMU", "Dexter Pressure 250", "Dexter Pressure 500", "Dexter Thermal IR",
		"Cruizcore Gyro", "RCX Temperature", "RCX Light"
		};
	
	private static final String[] sensorClasses = { "", "EV3TouchSensor",
		"EV3IRSensor", "EV3ColorSensor", "EV3UltrasonicSensor", "EV3GyroSensor",
		"NXTTouchSensor", "NXTSoundSensor", "NXTLightSensor", "NXTColorSensor", "NXTUltrasonicSensor",
		"HiTechnicGyro", "HiTechnicAccelerometer", "HiTechnicColorSensor",
		"HiTechnicCompassSensor", "HiTechnicBarometer", "HiTechnicAngleSensor",
		"HiTechnicMagneticSensor","HiTechnicIRSeekerv2", "HiTechnicEOPD",
		"MindsensorsAccelerometer", "MindsensorsCompass", "MindsensorsDistanceSensor",
		"MindsensorsLineLeader", "MindsensorsPressureSensor",
		"DexterIMU", "DexterPressureSensor250", "DexterPressureSensor500", "DexterThermalIRSensor",
		"CruizcoreGyro", "RCXThermometer", "RCXLightSensor"
		};

	private static final String[] sensorPorts = { "S1", "S2", "S3", "S4" };
	
	private JComboBox<String> sensorTypeList = new JComboBox<String>(sensorTypes);
	
	private String[] sensorModes = {};
	
	private JComboBox<String> sensorModeList;
	
	private Border etchedBorder = BorderFactory.createEtchedBorder();
	
	private int portNumber;
	
	private BaseSensor sensor;
	private RemoteEV3 ev3;
	private JLabel sensorValue = new JLabel();
	
	public SensorPanel(int portNumber) {
		super();
		this.portNumber = portNumber;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel setSensorLabel = new JLabel(sensorPorts[portNumber]);
		JPanel labelPanel = new JPanel();
		labelPanel.add(setSensorLabel);
		add(labelPanel);
		JPanel typePanel = new JPanel();
		JLabel typeLabel = new JLabel("Sensor:");
		typePanel.add(typeLabel);
		typePanel.add(sensorTypeList);
		add(typePanel);
		JPanel modePanel = new JPanel();
		JLabel modeLabel = new JLabel("Mode:");
		modePanel.add(modeLabel);
		sensorModeList = new JComboBox<String>(sensorModes);
		modePanel.add(sensorModeList);
		add(modePanel);
		JPanel valuePanel = new JPanel();
		JLabel valueLabel = new JLabel("Value:");
		valuePanel.add(valueLabel);
		valuePanel.add(sensorValue);
		add(valuePanel);
		setBorder(etchedBorder);
		sensorTypeList.addItemListener(this);
	}
	
	public void setEV3(RemoteEV3 ev3) {
		this.ev3 = ev3;
	}
	
	public void update() {	
		if (sensor != null) {
			int selected = sensorModeList.getSelectedIndex();
			SensorMode m = sensor.getMode(selected);
			float[] sample = new float[m.sampleSize()];
			m.fetchSample(sample, 0);
			
			StringBuilder sb = new StringBuilder();
			DecimalFormat df= new DecimalFormat( "#,###,###,##0.00" );
			
			for(int j=0;j<m.sampleSize();j++) {
				sb.append(df.format(sample[j]));
				if (j<m.sampleSize()-1) sb.append(',');
			}
			
			sensorValue.setText(sb.toString());		
		}
	}

	public void close() {
		if (sensor != null) sensor.close();
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {
		if (ev3 == null) return;
	    if (event.getStateChange() == ItemEvent.SELECTED) {
		    int selected = sensorTypeList.getSelectedIndex();
		   
		    if (sensor != null) {
		    	sensor.close();
		    	sensor = null;
		    }
		   
		    if (selected == 0) return;
		   
			String className = "lejos.hardware.sensor." + sensorClasses[selected];
			System.out.println("Sensor type is " + sensorTypes[selected]);
			System.out.println("Sensor class is " + className);
			
			String portName = sensorPorts[portNumber];
			
			System.out.println("Port is " + portName);
			
			Port p = ev3.getPort(portName);
			
			
			try {
				Class<?> c = Class.forName(className);
				Class<?>[] params = new Class<?>[1];
				params[0] = Port.class;
				Constructor<?> con = c.getConstructor(params);
				System.out.println("Created constructor");
				Object[] args = new Object[1];
				args[0] = p;
				BaseSensor s = (BaseSensor) con.newInstance(args);
				System .out.println("Created sensor class");
				
				sensorModeList.removeAllItems();
				
				for(String m: s.getAvailableModes()) {
					sensorModeList.addItem(m);
				}
				
				revalidate();
				sensor = s;		
				
			} catch (Exception e) {
				System.err.println("Failed to create sensor class: " + e);
				System.err.println("Cause: " + e.getCause());
				e.getCause().printStackTrace();
			}	
	   }
	}
}
