package lejos.ev3.tools;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.remote.ev3.RMIMenu;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.nxt.NXTProtocol;

/**
 * 
 * Graphical control center for leJOS NXJ.
 * 
 * @author Lawrie Griffiths
 */
public class EV3Control implements ListSelectionListener, NXTProtocol, ConsoleViewerUI {
	// Constants
    private static final int LCD_WIDTH = 100;
    private static final int LCD_HEIGHT = 64;
    
	private static final int RMI = 0;
	private static final int RCONSOLE = 1;
	
	private static final String defaultProgramProperty = "lejos.default_program";
	private static final String defaultProgramAutoRunProperty = "lejos.default_autoRun";
	private static final String sleepTimeProperty = "lejos.sleep_time";
	private static final String pinProperty = "lejos.bluetooth_pin";
	
	private static final Dimension frameSize = new Dimension(800, 620);
	private static final Dimension filesAreaSize = new Dimension(780, 300);
	private static final Dimension filesPanelSize = new Dimension(500, 400);
	private static final Dimension nxtButtonsPanelSize = new Dimension(220, 130);
	private static final Dimension filesButtonsPanelSize = new Dimension(770,100);
	private static final Dimension nxtTableSize = new Dimension(500, 100);	
	private static final Dimension labelSize = new Dimension(60, 20);
	private static final Dimension sliderSize = new Dimension(150, 50);
	private static final Dimension tachoSize = new Dimension(100, 20);
	private static final Dimension infoPanelSize = new Dimension(300, 110);
	private static final Dimension namePanelSize = new Dimension(300, 110);
	private static final Dimension innerInfoPanelSize = new Dimension(280, 70);
	private static final Dimension tonePanelSize = new Dimension(300, 110);
	private static final Dimension i2cPanelSize = new Dimension(480, 170);
	private static final Dimension volumePanelSize = new Dimension(180,150);
	private static final Dimension sleepPanelSize = new Dimension(180,150);
	private static final Dimension defaultProgramPanelSize = new Dimension(250,150);
	
	private static final int fileNameColumnWidth = 400;
	
	private static final String title = "EV3 Control Center";

	private static final String[] sensorTypes = { "No Sensor", "Touch Sensor",
			"Temperature", "RCX Light", "RCX Rotation", "Light Active",
			"Light Inactive", "Sound DB", "Sound DBA", "Custom", "I2C",
			"I2C 9V" };

	private static final int[] sensorTypeValues = { NO_SENSOR, SWITCH, TEMPERATURE,
			REFLECTION, ANGLE, LIGHT_ACTIVE, LIGHT_INACTIVE, SOUND_DB,
			SOUND_DBA, CUSTOM, LOWSPEED, LOWSPEED_9V };

	private static final String[] sensors = { "S1", "S2", "S3", "S4" };

	private static final String[] sensorModes = { "Raw", "Boolean", "Percentage" };

	private static final int[] sensorModeValues = { RAWMODE, BOOLEANMODE, PCTFULLSCALEMODE };
	
	private final String[] motorNames = { "A", "B", "C", "D" };
	
	private final String[] volumeLevels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	// GUI components
	private Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private JFrame frame = new JFrame(title);
	private JTable nxtTable = new JTable();
	private JScrollPane nxtTablePane;
	private JTextField nameText = new JTextField(8);
	private JTable table;
	private JScrollPane tablePane;
	private JPanel filesPanel = new JPanel();
	private JPanel settingsPanel = new JPanel();
	private JPanel volumePanel = new JPanel();
	private JPanel sleepPanel = new JPanel();
	private JPanel defaultProgramPanel = new JPanel();
	private JPanel consolePanel = new JPanel();
	private JPanel monitorPanel = new JPanel();
	private JPanel controlPanel = new JPanel();
	private JPanel dataPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	private JTextArea theConsoleLog = new JTextArea(16, 68);
	private JTextArea theDataLog = new JTextArea(20, 68);
	private LabeledGauge batteryGauge = new LabeledGauge("Battery", 10000);
	private JSlider[] sliders = new JSlider[4];
	private JLabel[] tachos = new JLabel[4];
	private JCheckBox[] selectors = new JCheckBox[4];
	private JCheckBox[] reversors = new JCheckBox[4];
	private JTextField[] limits = new JTextField[4];
	private JButton[] resetButtons = new JButton[4];
	private JButton connectButton = new JButton("Connect");
	private JButton dataDownloadButton = new JButton("Download");
	private TextField dataColumns = new TextField("8", 2);
	private JButton searchButton = new JButton("Search");
	private JButton monitorUpdateButton = new JButton("Update");
	private JButton forwardButton = new JButton("Forward");
	private JButton backwardButton = new JButton("Backward");
	private JButton leftButton = new JButton("Turn Left");
	private JButton rightButton = new JButton("Turn Right");
	private JButton deleteButton = new JButton("Delete Files");
	private JButton uploadButton = new JButton("Upload file");
	private JButton downloadButton = new JButton("Download file");
	private JButton runButton = new JButton("Run program");
	private JButton nameButton = new JButton("Set Name");
	private JButton formatButton = new JButton("Format");
	private JButton setDefaultButton = new JButton("Set Default");
	private JRadioButton rmiButton = new JRadioButton("RMI", true);
	private JRadioButton rconsoleButton = new JRadioButton("RConsole");
	private JFormattedTextField freq = new JFormattedTextField(new Integer(500));
	private JFormattedTextField duration = new JFormattedTextField(new Integer(1000));
	private JComboBox sensorList = new JComboBox(sensors);
	private JComboBox sensorList2 = new JComboBox(sensors);
	private JComboBox sensorModeList = new JComboBox(sensorModes);
	private JComboBox sensorTypeList = new JComboBox(sensorTypes);
	private SensorPanel[] sensorPanels = { new SensorPanel("Sensor Port 1"),
			new SensorPanel("Sensor Port 2"), new SensorPanel("Sensor Port 3"),
			new SensorPanel("Sensor Port 4") };
	private JFormattedTextField txData = new JFormattedTextField();
	private JFormattedTextField rxDataLength = new JFormattedTextField(new Integer(1));
	private JFormattedTextField address;
	private JLabel rxData = new JLabel();
	private Border etchedBorder = BorderFactory.createEtchedBorder();
	private JButton soundButton = new JButton("Play Sound File");
	private JTextField newName = new JTextField(16);
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	// Other instance data
	private ExtendedFileModel fm;
	private EV3Control control;
	private int mv;
	private int appProtocol = RMI;
	private int rowLength = 8; // default
	private int recordCount;;
	private ConsoleViewComms cvc;
	private ConsoleViewComms[] cvcs;
	private LCDDisplay lcd;
	private File directoryLastUsed;
	private Socket sock;
	private static final int PORT = 8001;
	
	private RMIMenu menu;
    private RemoteEV3 ev3;
    
	// Formatter
	private static final NumberFormat FORMAT_FLOAT = NumberFormat.getNumberInstance();

	/**
	 * Command line entry point
	 */
	public static void main(String args[])
	{
		ToolStarter.startSwingTool(EV3Control.class, args);
	}
	
	public static int start(String[] args)
	{
		return new EV3Control().run();
	}

	/**
	 * Run the program
	 */
	private int run() {
		// Close connection and exit when frame windows closed
		WindowListener listener = new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				closeAll();
				System.exit(0);
			}
		};
		frame.addWindowListener(listener);

		control = this;

		// Search Button: search for NXTs
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				search();
			}
		});

		// Connect Button: connect to selected NXT
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				connect();
			}
		});

		// Monitor Update Button: get values being monitored
		monitorUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				getSensorValues();
				updateSensors();
			}
		});
		
		rmiButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (appProtocol == getAppProtocol()) return;
				if (rmiButton.isSelected()) {
					createRMITabs();
					appProtocol = RMI;
				}				
			}		
		});
		
		rconsoleButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (appProtocol == getAppProtocol()) return;
				if (rconsoleButton.isSelected()) {
					createConsoleTabs();
					appProtocol = RCONSOLE;
				}				
			}		
		});

		// Create the panels
		createNXTSelectionPanel();
		createConsolePanel();
		createDataPanel();
		createMonitorPanel();
		createControlPanel();
		createMiscellaneousPanel();
		createSettingsPanel();

		// set the size of the files panel
		filesPanel.setPreferredSize(filesPanelSize);
		
		// Set up the frame
		frame.setPreferredSize(frameSize);
		
		createRMITabs();
		
		frame.add(tabbedPane);
		frame.pack();
		frame.setVisible(true);
		return 0;
	}

	/**
	 * Get files from the NXT and display them in the files panel
	 */
	private void showFiles() {
		// Layout and populate files table
		createFilesTable("/home/root/lejos/samples/");

		// Remove current content of files panel and recreate it
		filesPanel.removeAll();
		createFilesPanel();
		
		// Recreate miscellaneous and settings panel
		otherPanel.removeAll();
		createMiscellaneousPanel();
		settingsPanel.removeAll();
		volumePanel.removeAll();
		sleepPanel.removeAll();
		defaultProgramPanel.removeAll();
		createSettingsPanel();
		
		// Process buttons

		// Delete Button: delete a file from the NXT
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				deleteFiles();
			}
		});
		
		// Set Default button
		setDefaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setDefaultProgram();
			}
		});

		// Upload Button: upload a file to the NXT
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				upload();
			}
		});

		// Download Button: download a file from from the NXT
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				download();
			}
		});

		// Run Button: run a program on the NXT
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				runFile();
			}
		});

		// Set Name Button: set a new name for the NXT
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				rename(newName.getText());
			}
		});
		
		// Sound button: Play Sound file
		soundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSoundFile();
			}
		});
		
		// Format button; Delete user flash memory
		formatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				format();
			}
		});

		// Pack the frame
		//frame.pack();
	}

	/**
	 * Lay out NXT Selection panel
	 */
	private void createNXTSelectionPanel() {
		JPanel nxtPanel = new JPanel();
		nxtTablePane = new JScrollPane(nxtTable);
		nxtTablePane.setPreferredSize(nxtTableSize);
		nxtPanel.add(new JScrollPane(nxtTablePane), BorderLayout.WEST);
		frame.getContentPane().add(nxtPanel, BorderLayout.NORTH);
		nxtTable.setPreferredScrollableViewportSize(nxtButtonsPanelSize);
		JLabel nameLabel = new JLabel("Name: ");
		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(nameText);
		JPanel nxtButtonPanel = new JPanel();
		nxtButtonPanel.add(namePanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(searchButton);
		buttonPanel.add(connectButton);
		nxtButtonPanel.add(buttonPanel);
		ButtonGroup protocolButtonGroup = new ButtonGroup();
		nxtButtonPanel.add(rmiButton);
		nxtButtonPanel.add(rconsoleButton);
		ButtonGroup appProtocolButtonGroup = new ButtonGroup();
		appProtocolButtonGroup.add(rmiButton);
		appProtocolButtonGroup.add(rconsoleButton);
		nxtButtonPanel.setPreferredSize(nxtButtonsPanelSize);
		nxtPanel.add(nxtButtonPanel, BorderLayout.EAST);
	}

	/**
	 *  Lay out Console Panel
	 */
	private void createConsolePanel() {
		JLabel consoleTitleLabel = new JLabel("Output from RConsole");
		consolePanel.add(consoleTitleLabel);
		consolePanel.add(new JScrollPane(theConsoleLog));
        lcd = new LCDDisplay();
        lcd.clear();
        lcd.setMinimumSize(new Dimension(LCD_WIDTH*2, LCD_HEIGHT*2));
        lcd.setEnabled(true);
        lcd.setPreferredSize(lcd.getMinimumSize());
        consolePanel.add(lcd);
	}

	/**
	 *  Lay out Data Console Panel
	 */
	private void createDataPanel() {
		JLabel dataTitleLabel = new JLabel("Data Log");
		dataPanel.add(dataTitleLabel, BorderLayout.NORTH);
		dataPanel.add(new JScrollPane(theDataLog), BorderLayout.CENTER);
		JPanel commandPanel = new JPanel();
		commandPanel.add(new JLabel("Columns:"));
		commandPanel.add(dataColumns);
		commandPanel.add(dataDownloadButton);
		dataPanel.add(commandPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Layout for settings panel
	 */
	private void createSettingsPanel() {
		createVolumePanel();
		createSleepPanel();
		createDefaultProgramPanel();
		settingsPanel.add(volumePanel);
		settingsPanel.add(sleepPanel);
		settingsPanel.add(defaultProgramPanel);
	}
	
	/**
	 * Layout for sound volume levels
	 */
	private void createVolumePanel() {
		volumePanel.setBorder(etchedBorder);
		JLabel volumesLabel = new JLabel("Volume settings");
		JLabel volumeLabel = new JLabel("Master Volume:");
		JPanel volumeDropdownPanel = new JPanel();
		final JComboBox volumeList = new JComboBox(volumeLevels);
		JLabel keyClickVolumeLabel = new JLabel("Key Click Volume:");
		final JComboBox volumeList2 = new JComboBox(volumeLevels);
		volumePanel.add(volumesLabel);
		volumeDropdownPanel.add(volumeLabel);
		volumeDropdownPanel.add(volumeList);
		volumePanel.add(volumeDropdownPanel);
		JPanel clickDropdownPanel = new JPanel();
		clickDropdownPanel.add(keyClickVolumeLabel);
		clickDropdownPanel.add(volumeList2);
		volumePanel.add(clickDropdownPanel);
		volumePanel.setPreferredSize(volumePanelSize);
		
		if (menu != null) {
			try {
				int volume = Integer.parseInt(menu.getSetting(Sound.VOL_SETTING));
				volumeList.setSelectedIndex(volume/10);
				int keyClickVolume = Integer.parseInt(menu.getSetting(Button.VOL_SETTING));
				volumeList2.setSelectedIndex(keyClickVolume/10);
			} catch(IOException e) {
				showMessage("Failed to get volume settings");
			}
		}
		
		JButton setButton = new JButton("Set");
		volumePanel.add(setButton);
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
	
	/**
	 * Layout for sleep timer panel
	 */
	private void createSleepPanel() {
		sleepPanel.setBorder(etchedBorder);
		JLabel menuSettings = new JLabel("Menu Settings");
		JLabel sleepLabel = new JLabel("Menu sleep time:");
		final JComboBox sleepList = new JComboBox(volumeLevels);
		sleepPanel.add(menuSettings);
		JPanel sleepDropdownPanel = new JPanel();
		sleepDropdownPanel.add(sleepLabel);
		sleepDropdownPanel.add(sleepList);
		sleepPanel.add(sleepDropdownPanel);
		sleepPanel.setPreferredSize(sleepPanelSize);
		JButton setButton = new JButton("Set");
		sleepPanel.add(setButton);
		
		if (menu != null) {
			try {
				int sleep = Integer.parseInt(menu.getSetting(sleepTimeProperty));
				if (sleep >= 0 && sleep <= 10) sleepList.setSelectedIndex(sleep);
			} catch(Exception e) {
				showMessage("Failed to get sleep timer setting");
			}
		}
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
	
	private void createDefaultProgramPanel() {
		defaultProgramPanel.setBorder(etchedBorder);
		defaultProgramPanel.setPreferredSize(defaultProgramPanelSize);
		JPanel labelDefaultPanel = new JPanel();
		JLabel defaultProgramSettings = new JLabel("Default Program Settings");
		defaultProgramPanel.add(defaultProgramSettings);
		JPanel defProgPanel = new JPanel();
		JLabel defaultProgramLabel = new JLabel("Default program:");
		JLabel defaultProgram = new JLabel();
		defProgPanel.add(defaultProgramLabel);
		defProgPanel.add(defaultProgram);
		labelDefaultPanel.add(defProgPanel);
		defaultProgramPanel.add(labelDefaultPanel);
		final JCheckBox autoRun = new JCheckBox();
		
		if (menu != null) {
			try {
				String defProg = menu.getSetting(defaultProgramProperty);
				defaultProgram.setText(defProg);
				if (defProg.length() > 0) {
					JPanel autoRunPanel = new JPanel();
					JLabel autoRunLabel = new JLabel("Auto Run:");
					autoRunPanel.add(autoRunLabel);
					autoRunPanel.add(autoRun);
					defaultProgramPanel.add(autoRunPanel);
					boolean autoRunSetting = Boolean.parseBoolean(menu.getSetting(defaultProgramAutoRunProperty));
					autoRun.setSelected(autoRunSetting);
					
					JButton setButton = new JButton("Set");
					defaultProgramPanel.add(setButton);
					
					setButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								if (menu == null) return;
								menu.setSetting(defaultProgramAutoRunProperty, Boolean.toString(autoRun.isSelected()));
							} catch (Exception ioe) {
								showMessage("Failed to set default program settings");
							}
						}
					});
				}
			} catch(IOException e) {
				showMessage("Failed to get default program settings");
			}
		}
	}

	/**
	 *  Lay out Monitor Panel
	 */
	private void createMonitorPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JPanel batteryPanel = new JPanel();
		batteryPanel.setBorder(etchedBorder);
		batteryPanel.add(batteryGauge);
		leftPanel.add(batteryPanel);
		JPanel setSensorPanel = new JPanel();
		setSensorPanel.setBorder(etchedBorder);
		setSensorPanel.setLayout(new BoxLayout(setSensorPanel, BoxLayout.Y_AXIS));
		JLabel setSensorLabel = new JLabel("Set Sensor type & mode");
		JPanel labelPanel = new JPanel();
		labelPanel.add(setSensorLabel);
		setSensorPanel.add(labelPanel);
		JPanel portPanel = new JPanel();
		JLabel portLabel = new JLabel("Port:");
		portPanel.add(portLabel);
		portPanel.add(sensorList2);
		setSensorPanel.add(portPanel);
		JPanel typePanel = new JPanel();
		JLabel typeLabel = new JLabel("Type:");
		typePanel.add(typeLabel);
		typePanel.add(sensorTypeList);
		setSensorPanel.add(typePanel);
		JPanel modePanel = new JPanel();
		JLabel modeLabel = new JLabel("Mode:");
		modePanel.add(modeLabel);
		modePanel.add(sensorModeList);
		setSensorPanel.add(modePanel);
		JButton setSensorButton = new JButton("Set Sensor");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(setSensorButton);
		setSensorPanel.add(buttonPanel);
		leftPanel.add(setSensorPanel);
		
		setSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSensor();
			}
		});
		
		monitorPanel.add(leftPanel);
		for (int i = 0; i < 4; i++) {
			monitorPanel.add(sensorPanels[i]);
		}
		monitorPanel.add(monitorUpdateButton);
	}

	/**
	 *  Create the tabs for RMI
	 */
	private void createRMITabs() {
		tabbedPane.removeAll();
		tabbedPane.addTab("Files", filesPanel);
		tabbedPane.addTab("Settings", settingsPanel);
		tabbedPane.addTab("Monitor", monitorPanel);
		tabbedPane.addTab("Control", controlPanel);
		tabbedPane.addTab("Miscellaneous", otherPanel);
	}
	
	/**
	 *  Create the tabs for RConsole
	 */
	private void createConsoleTabs() {
		tabbedPane.removeAll();
		tabbedPane.addTab("Console", consolePanel);
	}
	
	/**
	 *  Set up the files panel
	 */
	private void createFilesPanel() {
		filesPanel.add(tablePane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(deleteButton);
		buttonPanel.add(uploadButton);
		buttonPanel.add(downloadButton);
		buttonPanel.add(runButton);
		buttonPanel.add(soundButton);
		buttonPanel.add(formatButton);
		buttonPanel.add(setDefaultButton);
		buttonPanel.setPreferredSize(filesButtonsPanelSize);
		filesPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 *  Populate the Other Panel
	 */
	private void createMiscellaneousPanel() {
		createInfoPanel();
		createTonePanel();
		createI2cPanel();
		createNamePanel();
	}
	
	/**
	 * Create rename NXT panel
	 */
	private void createNamePanel() {
		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(namePanelSize);
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
		namePanel.setBorder(etchedBorder);
		JPanel titlePanel = new JPanel();
		JLabel title = new JLabel("Change Friendly Name");
		JPanel newNamePanel = new JPanel();
		JLabel nameLabel = new JLabel("New name:");
		titlePanel.add(title);
		namePanel.add(titlePanel);
		newNamePanel.add(nameLabel);
		newNamePanel.add(newName);
		namePanel.add(newNamePanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nameButton);
		namePanel.add(buttonPanel);
		otherPanel.add(namePanel);		
	}
	
	/**
	 * Create Info Panel
	 */
	private void createInfoPanel() {
		JPanel infoPanel = new JPanel();

		infoPanel.setLayout(new GridLayout(4, 2));
		JLabel freeFlashLabel = new JLabel("Free flash:  ");
		String firmwareVersionString = "Unknown";
		String menuVersionString = "Unknown";
		String freeFlashString = "Unknown";
		
		JLabel freeFlash = new JLabel(freeFlashString);
		infoPanel.add(freeFlashLabel);
		infoPanel.add(freeFlash);
		JLabel firmwareVersionLabel = new JLabel("Firmware version:");
		JLabel firmwareVersion = new JLabel(firmwareVersionString);
		infoPanel.add(firmwareVersionLabel);
		infoPanel.add(firmwareVersion);
		JLabel menuVersionLabel = new JLabel("Menu version");
		JLabel menuVersion = new JLabel(menuVersionString);
		infoPanel.add(menuVersionLabel);
		infoPanel.add(menuVersion);
		infoPanel.setPreferredSize(innerInfoPanelSize);
		JPanel outerInfoPanel = new JPanel();
		outerInfoPanel.setPreferredSize(infoPanelSize);
		outerInfoPanel.setBorder(etchedBorder);
		JPanel headingPanel = new JPanel();
		JLabel headingLabel = new JLabel("Brick Information");
		headingPanel.add(headingLabel);
		outerInfoPanel.add(headingPanel);
		outerInfoPanel.add(infoPanel);
		otherPanel.add(outerInfoPanel);
	}
	
	/**
	 * Create play tone panel
	 */
	private void createTonePanel() {
		JPanel tonePanel = new JPanel();
		tonePanel.setLayout(new BoxLayout(tonePanel, BoxLayout.Y_AXIS));
		JPanel toneHeading = new JPanel();
		JLabel toneLabel = new JLabel("Play tone");
		toneHeading.add(toneLabel);
		tonePanel.add(toneHeading);
		JPanel freqPanel = new JPanel();
		JLabel freqLabel = new JLabel("Frequency:");
		freq.setColumns(5);
		freqLabel.setLabelFor(freq);
		JLabel durationLabel = new JLabel("Duration:");
		duration.setColumns(5);
		durationLabel.setLabelFor(duration);
		JButton play = new JButton("Play tone");
		freqPanel.add(freqLabel);
		freqPanel.add(freq);
		freqPanel.add(durationLabel);
		freqPanel.add(duration);
		tonePanel.add(freqPanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(play);
		tonePanel.add(buttonPanel);
		tonePanel.setPreferredSize(tonePanelSize);
		tonePanel.setBorder(etchedBorder);
		otherPanel.add(tonePanel);
		
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playTone();
			}
		});
	}
	
	/**
	 * Create panel for I2C 
	 */
	private void createI2cPanel() {
		JPanel i2cPanel = new JPanel();
		i2cPanel.setLayout(new BoxLayout(i2cPanel, BoxLayout.Y_AXIS));
		JPanel labelPanel = new JPanel();
		JLabel i2cTester = new JLabel("I2C Device Tester");
		labelPanel.add(i2cTester);
		i2cPanel.add(labelPanel);
		JPanel topPanel = new JPanel();
		JPanel sensorSelectPanel = new JPanel();
		JLabel sensorLabel = new JLabel("Port:");
		sensorSelectPanel.add(sensorLabel);
		sensorSelectPanel.add(sensorList);
		JLabel addressLabel = new JLabel("Address:");
		address = new JFormattedTextField(new Integer(2));
		address.setColumns(2);
		sensorSelectPanel.add(addressLabel);
		sensorSelectPanel.add(address);
		topPanel.add(sensorSelectPanel);
		JPanel rxlPanel = new JPanel();
		JLabel rxlLabel = new JLabel("RxData length:");
		rxlLabel.setLabelFor(rxDataLength);
		rxlPanel.add(rxlLabel);
		rxlPanel.add(rxDataLength);
		topPanel.add(rxlPanel);
		i2cPanel.add(topPanel);
		txData.setColumns(32);
		JPanel txPanel = new JPanel();
		JLabel txLabel = new JLabel("Send (hex):");
		txLabel.setLabelFor(txData);
		txPanel.add(txLabel);
		txPanel.add(txData);
		i2cPanel.add(txPanel);
		rxDataLength.setColumns(2);
		JPanel rxPanel = new JPanel();
		JLabel rxLabel = new JLabel("Received (hex):");
		rxLabel.setLabelFor(rxData);
		rxPanel.add(rxLabel);
		rxPanel.add(rxData);
		i2cPanel.add(rxPanel);
		JButton txDataSend = new JButton("Send");
		JButton i2cStatus = new JButton("Status");
		JButton rxDataReceive = new JButton("Receive");
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(txDataSend);
		buttonsPanel.add(i2cStatus);
		buttonsPanel.add(rxDataReceive);
		i2cPanel.add(buttonsPanel);
		i2cPanel.setBorder(BorderFactory.createEtchedBorder());
		i2cPanel.setPreferredSize(i2cPanelSize);
		otherPanel.add(i2cPanel);
		
		txDataSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				i2cSend();
			}
		});
		
		rxDataReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				i2cReceive();
			}
		});
		
		i2cStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				i2cStatus();
			}
		});
	}

	/**
	 *  Set up the files table
	 */
	private void createFilesTable(String directory) {
		fm = new ExtendedFileModel();
		String[] programs;
		try {
			programs = menu.getSampleNames();
			long[] sizes = new long[programs.length];
			for(int i=0;i<sizes.length;i++) {
				sizes[i] = menu.getFileSize(directory + programs[i]);
			}
			fm.fetchFiles(programs, sizes);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		table = new JTable(fm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(fileNameColumnWidth);
		tablePane = new JScrollPane(table);
		tablePane.setPreferredSize(filesAreaSize);
		
		// Change first parameter to System.out to enable debugging
        new FileDrop( null, tablePane, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   
            		String fileName = files[i].getName();
            		int row = fm.getRow(fileName);
            		try {
            			if (row >= 0) fm.delete(fileName, row);
            		} catch (IOException e) {
            			showMessage("IOException deleting file");
            		}
                	uploadFile(files[i]);
                }
            }
        }); 
	}

	/**
	 * Create a panel for motor control
	 */
	private JPanel createMotorPanel(int index) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final JSlider slider = new JSlider(0, 100);
		sliders[index] = slider;
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		JLabel label = new JLabel("   " + motorNames[index]);
		label.setPreferredSize(labelSize);
		panel.add(label);
		final JLabel value = new JLabel("    " + slider.getValue());
		value.setPreferredSize(labelSize);
		panel.add(value);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent c) {
				value.setText(String.format("%6d", slider.getValue()));
			}
		});
		
		slider.setPreferredSize(sliderSize);
		panel.add(slider);
		JLabel tacho = new JLabel("");
		tacho.setPreferredSize(tachoSize);
		tachos[index] = tacho;
		panel.add(tacho);
		JCheckBox selected = new JCheckBox();
		selectors[index] = selected;
		selected.setPreferredSize(labelSize);
		panel.add(selected);
		JCheckBox reverse = new JCheckBox();
		reverse.setPreferredSize(labelSize);
		reversors[index] = reverse;
		panel.add(reverse);
		JTextField limit = new JTextField(6);
		limit.setMaximumSize(new Dimension(60, 20));
		limits[index] = limit;
		panel.add(limit);
		JButton resetButton = new JButton("Reset");
		resetButtons[index] = resetButton;
		panel.add(resetButton);
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetTacho((JButton) e.getSource());
			}
		});
		
		panel.setBorder(BorderFactory.createEtchedBorder());
		return panel;
	}

	/**
	 * Create the header line for the motors
	 */
	private JPanel createMotorsHeader() {
		JPanel labelPanel = new JPanel();
		JLabel motorLabel = new JLabel("Motor");
		motorLabel.setPreferredSize(labelSize);
		JLabel speedLabel = new JLabel("Speed");
		speedLabel.setPreferredSize(labelSize);
		JLabel sliderLabel = new JLabel("          Set speed");
		sliderLabel.setPreferredSize(sliderSize);
		JLabel tachoLabel = new JLabel("Tachometer");
		tachoLabel.setPreferredSize(tachoSize);
		JLabel selectedLabel = new JLabel("Selected");
		selectedLabel.setPreferredSize(labelSize);
		JLabel reverseLabel = new JLabel("Reverse");
		reverseLabel.setPreferredSize(labelSize);
		JLabel limitLabel = new JLabel("Limit");
		limitLabel.setPreferredSize(labelSize);
		JLabel resetLabel = new JLabel("Reset");
		resetLabel.setPreferredSize(labelSize);
		labelPanel.add(motorLabel);
		labelPanel.add(speedLabel);
		labelPanel.add(sliderLabel);
		labelPanel.add(tachoLabel);
		labelPanel.add(selectedLabel);
		labelPanel.add(reverseLabel);
		labelPanel.add(limitLabel);
		labelPanel.add(resetLabel);
		return labelPanel;
	}

	/**
	 * Create the control panel
	 */
	private void createControlPanel() {
		JPanel motorsPanel = new JPanel();
		motorsPanel.setLayout(new BoxLayout(motorsPanel, BoxLayout.Y_AXIS));
		motorsPanel.add(createMotorsHeader());
		for (int i = 0; i < 4; i++) {
			motorsPanel.add(createMotorPanel(i));
		}
		JPanel buttonsPanel = new JPanel();
		controlPanel.add(motorsPanel);
		buttonsPanel.add(forwardButton);
		buttonsPanel.add(backwardButton);
		buttonsPanel.add(leftButton);
		buttonsPanel.add(rightButton);
		controlPanel.add(buttonsPanel);

		forwardButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!aMotorSelected()) return;
				int[] speed = getSpeeds();
				move(speed[0], speed[1], speed[2]);
			}

			public void mouseReleased(MouseEvent e) {
				stopMotors();
			}
		});

		backwardButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!aMotorSelected()) return;
				int[] speed = getSpeeds();
				move(-speed[0], -speed[1], -speed[2]);
			}

			public void mouseReleased(MouseEvent e) {
				stopMotors();
			}
		});

		leftButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!twoMotorsSelected()) return;
				int[] speed = getSpeeds();
				int[] multipliers = leftMultipliers();
				move(speed[0] * multipliers[0], speed[1] * multipliers[1], speed[2] * multipliers[2]);
			}

			public void mouseReleased(MouseEvent e) {
				stopMotors();
			}
		});

		rightButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!twoMotorsSelected()) return;
				int[] speed = getSpeeds();
				int[] multipliers = rightMultipliers();
				move(speed[0] * multipliers[0], speed[1] * multipliers[1], speed[2] * multipliers[2]);
			}

			public void mouseReleased(MouseEvent e) {
				stopMotors();
			}
		});
	}
	
	/**
	 * Return the number of motors selected
	 */
	private int numMotorsSelected() {
		int numSelected = 0;
		
		for(int i=0;i<3;i++) {
			if (selectors[i].isSelected()) numSelected ++;
		}
		
		return numSelected;
	}
	
	/**
	 * Return true iff exactly two motors selected and show message if not
	 */
	private boolean twoMotorsSelected() {
		if (numMotorsSelected() != 2) {
			showMessage("Exactly two motors must be selected");
			return false;	
		}
		return true;
	}
	
	/**
	 * Return true iff at least one motor selected and show message if not
	 */
	private boolean aMotorSelected() {
		if (numMotorsSelected() < 1) {
			showMessage("At least one motor must be selected");
			return false;	
		}
		return true;
	}
	
	/**
	 * Calculate speed multipliers for turning left
	 */
	private int[] leftMultipliers() {
		int[] multipliers = new int[3];
		boolean firstFound = false, secondFound = false;
		
		for(int i=0;i<3;i++) {
			if (selectors[i].isSelected() && !firstFound) {
				firstFound = true;
				multipliers[i] = -1;
			} else if (selectors[i].isSelected() && !secondFound) {
				secondFound = true;
				multipliers[i] = 1;
			} else {
				multipliers[i] = 0;
			}			
		}
		return multipliers;
	}
	
	
	/**
	 * Calculate the speed multipliers for turning right
	 */
	private int[] rightMultipliers() {
		int[] multipliers = new int[3];
		boolean firstFound = false, secondFound = false;
		
		for(int i=0;i<3;i++) {
			if (selectors[i].isSelected() && !firstFound) {
				firstFound = true;
				multipliers[i] = 1;
			} else if (selectors[i].isSelected() && !secondFound) {
				secondFound = true;
				multipliers[i] = -1;
			} else {
				multipliers[i] = 0;
			}			
		}
		return multipliers;
	}
	
	/**
	 * Download a file from the NXT
	 */
	private void getFile(File file, String fileName, int size) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] data = menu.fetchFile(fileName);
			out.write(data);
			out.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show a pop-up message
	 */
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	/**
	 * Update the sensor dials
	 */
	private void updateSensors() {
	}

	/**
	 * Clear the files tab.
	 */
	private void clearFiles() {
		filesPanel.removeAll();
		filesPanel.repaint();
	}

	/**
	 * Switch between EV3s in table of available EV3s
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			int row = nxtTable.getSelectedRow();
			if (row < 0) return;		
			
		}
	}

	/**
	 * Search for available NXTs and populate table with results.
	 */
	private void search() {
		closeAll();
		clearFiles();
		updateConnectButton(false);
	}

	/**
	 * Close all connections
	 */
	private void closeAll() {
	}

	/**
	 * Toggle Connect button between Connect and Disconnect
	 */
	private void updateConnectButton(boolean connected) {
		connectButton.setText((connected ? "Disconnect" : "Connect"));
	}
	
	/**
	 * Get the Application protocol
	 */
	private int getAppProtocol() {
		int appProtocol = 0;
		if (rmiButton.isSelected())	appProtocol = RMI;
		if (rconsoleButton.isSelected()) appProtocol = RCONSOLE;
		return appProtocol;
	}

	/**
	 * Stop the motors on the NXT and update the tachometer values
	 */
	private void stopMotors() {
	}

	/**
	 * Get an array of the tacho limit text boxes
	 */
	private int[] getLimits() {
		int[] lim = new int[3];

		for (int i = 0; i < 3; i++) {
			try {
				lim[i] = Integer.parseInt(limits[i].getText());
			} catch (NumberFormatException nfe) {
				lim[i] = 0;
			}
		}
		return lim;
	}
	
	/**
	 * Get an array of the speed slider values
	 */
	private int[] getSpeeds() {
		int[] speed = new int[3];

		for (int i = 0; i < 3; i++) {
			speed[i] = sliders[i].getValue();
			if (reversors[i].isSelected()) speed[i] = -speed[i];
		}
		return speed;
	}

	/**
	 * Retrieve the sensor and battery values from the NXT
	 */
	private void getSensorValues() {
	}

	/**
	 * Connect to the NXT
	 */
	private void connect() {
		String name = nameText.getText();
		if (name != null && name.length() > 0) {
			System.out.println("Connecting to " + name);
			try {
				menu = (RMIMenu) Naming.lookup("//" + name + "/RemoteMenu");
				ev3 = new RemoteEV3(name);
				showFiles();
			} catch (RemoteException | MalformedURLException
					| NotBoundException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Connect to RConsole
	 */
	private void consoleConnect() {		
		int row = nxtTable.getSelectedRow();
	}
	/**
	 * Append data item to the data log
	 */
	public void append(float x) {
		if (0 == recordCount % rowLength) theDataLog.append("\n");
		theDataLog.append(FORMAT_FLOAT.format(x) + "\t ");
		recordCount++;
	}
	
	/**
	 * Delete selected files
	 */
	private void deleteFiles() {
		frame.setCursor(hourglassCursor);
		try {
			for (int i = 0; i < fm.getRowCount(); i++) {
				Boolean b = (Boolean) fm.getValueAt(i,ExtendedFileModel.COL_DELETE);
				String fileName = (String) fm.getValueAt(i,ExtendedFileModel.COL_NAME);
				boolean deleteIt = b.booleanValue();
				if (deleteIt) {
					System.out.println("Deleting " + fileName);
					//menu.debugProgram(delete(fileName);
				}
			}
			//fm.fetchFiles(nxtCommand);
		} catch (Exception ioe) {
			showMessage("IOException deleting files");
		}
		frame.setCursor(normalCursor);
	}
	
	/**
	 * Choose a file and update it. Remember directory last used. 
	 */
	private void upload() {
		JFileChooser fc = new JFileChooser(directoryLastUsed);
		int returnVal = fc.showOpenDialog(frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			directoryLastUsed = file.getParentFile();
			uploadFile(file);
		}
	}
	
	/**
	 * Upload the specified file
	 */
	private void uploadFile(File file) {
		frame.setCursor(hourglassCursor);
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
		    in.read(data);
			menu.uploadFile(file.getName(), data);
		    in.close();
			//String msg = fm.fetchFiles(nxtCommand);
		} catch (IOException ioe) {
			showMessage("IOException uploading file");
		}
		frame.setCursor(normalCursor);
	}
	
	/**
	 * Download the selected file
	 */
	private void download() {
		int row = table.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fm.getFile(row).fileName;
		int size = fm.getFile(row).fileSize;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(new File(fileName));
		
		int returnVal = fc.showSaveDialog(frame);
		if (returnVal == 0) {
			File file = fc.getSelectedFile();
			frame.setCursor(hourglassCursor);
			getFile(file, fileName, size);
			frame.setCursor(normalCursor);
		}
	}
	
	/**
	 * Run the selected file.
	 */
	private void runFile() {
		int row = table.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		String name = fm.getFile(row).fileName;
		
		System.out.println("Running " + name);
		try {
			menu.runSample(name.replaceFirst(".jar", ""));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Change the friendly name of the NXT
	 */
	private void rename(String name) {
	}
	
	/**
	 * Move the motors
	 */
	private void move(int speed0, int speed1, int speed2 ) {
	}
	
	/**
	 * Set the sensor type and mode
	 */
	private void setSensor() {
	}
	
	/**
	 * Play a tone
	 */
	private void playTone() {
		try {
			ev3.getSound().playTone((Integer) freq.getValue(), (Integer) duration.getValue());
		} catch (IOException ioe) {
			showMessage("IO Exception playing tone");
		} catch (NumberFormatException nfe) {
			showMessage("Frequency and Duration must be integers");
		}
	}
	
	/**
	 * Reset the tachometer for a motor
	 */
	private void resetTacho(JButton b) {
	}
	
	/**
	 * Play a sound file
	 */
	private void playSoundFile() {
		int row = table.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fm.getFile(row).fileName;
		try {
			System.out.println("Playing file " + fileName);
			ev3.getSound().playSample("/home/root/lejos/samples/" + fileName);
		} catch (Exception ioe) {
			showMessage("IO Exception playing sound file");
		}
	}
	
	/**
	 * Send I2C request
	 */
	private void i2cSend() {
		byte[] addr = new byte[1];
		addr[0] = ((Number)address.getValue()).byteValue(); // default I2C address
		
		try {
			/*nxtCommand.LSWrite(
					(byte) sensorList.getSelectedIndex(),
					appendBytes(addr, fromHex(txData.getText())),
					((Number) rxDataLength.getValue()).byteValue());*/
		} catch (Exception ioe) {
			showMessage("IO Exception sending txData");
		}
	}
	
	/** 
	 * Send an i2c status request
	 */
	private void i2cStatus() {
	}
	
	/**
	 * Read i2c reply
	 */
	private void i2cReceive() {
	}
	
	/**
	 * Format the file system
	 */
	private void format() {
		try {
			//fm.fetchFiles(nxtCommand);
		} catch (Exception ioe) {
			showMessage("IO Exception formatting file system");
		}
	}
	
	private void noFileSelected() {
		showMessage("No file selected");
	}
	
	private void setDefaultProgram() {
		int row = table.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fm.getFile(row).fileName;
		try {
			menu.setSetting(defaultProgramProperty,"/home/lejos/programs/" + fileName);
		} catch (IOException ioe) {
			showMessage("IO setting default program");
		}
	}
	
	/**
	 * Used for console viewer
	 */
	public void logMessage(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Used by console viewer
	 */
	public void connectedTo(String name, String address) {
		System.out.println("Connected to " + name + "(" + address + ")");
	}

	/**
	 * Used by console viewer
	 */
	public void setStatus(String msg) {
		System.out.println("Status is " + msg);
	}

	/**
	 * Used by console viewer
	 */
	public void append(String value) {
		theConsoleLog.append(value);
		theConsoleLog.setCaretPosition(theConsoleLog.getDocument().getLength());
	}

	/**
	 * Used by console viewer
	 */
    public void updateLCD(byte[] buffer)
    {
    	lcd.update(buffer);
    }
}
