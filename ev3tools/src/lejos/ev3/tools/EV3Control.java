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
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.remote.ev3.RMIMenu;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RemoteI2CPort;
import lejos.remote.nxt.NXTProtocol;

/**
 * 
 * Graphical control center for leJOS on the EV3.
 * 
 * @author Lawrie Griffiths
 */
public class EV3Control implements ListSelectionListener, NXTProtocol, ConsoleViewerUI {
	// Constants
	
	private static final String PROGRAMS_DIR = "/home/lejos/programs/";
	private static final String SAMPLES_DIR = "/home/root/lejos/samples/";
	
    private static final int LCD_WIDTH = 178;
    private static final int LCD_HEIGHT = 128;
	
	private static final String defaultProgramProperty = "lejos.default_program";
	private static final String defaultProgramAutoRunProperty = "lejos.default_autoRun";
	private static final String sleepTimeProperty = "lejos.sleep_time";
	
	private static final Dimension frameSize = new Dimension(800, 620);
	private static final Dimension filesAreaSize = new Dimension(780, 350);
	private static final Dimension filesPanelSize = new Dimension(500, 500);
	private static final Dimension ev3ButtonsPanelSize = new Dimension(220, 130);
	private static final Dimension filesButtonsPanelSize = new Dimension(770,100);
	private static final Dimension ev3TableSize = new Dimension(500, 100);	
	private static final Dimension labelSize = new Dimension(60, 20);
	private static final Dimension sliderSize = new Dimension(150, 50);
	private static final Dimension tachoSize = new Dimension(100, 20);
	private static final Dimension infoPanelSize = new Dimension(300, 110);
	private static final Dimension namePanelSize = new Dimension(300, 110);
	private static final Dimension innerInfoPanelSize = new Dimension(280, 70);
	private static final Dimension tonePanelSize = new Dimension(300, 110);
	private static final Dimension i2cPanelSize = new Dimension(480, 170);
	private static final Dimension volumePanelSize = new Dimension(200,250);
	private static final Dimension sleepPanelSize = new Dimension(200,250);
	private static final Dimension defaultProgramPanelSize = new Dimension(200,250);
	
	private static final int fileNameColumnWidth = 627;
	
	private static final String title = "EV3 Control Center";

	private static final String[] sensorPorts = { "S1", "S2", "S3", "S4" };
	
	private final String[] motorNames = { "A", "B", "C", "D" };
	
	private final String[] volumeLevels = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
	
	private final String[] frequencyLevels = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100",
			                                  "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000"};
	
	private final String[] lengthLevels = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100",
                                           "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000"};
	// GUI components
	private Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private JFrame frame = new JFrame(title);
	private JTable ev3Table = new JTable();
	private JScrollPane ev3TablePane;
	private JTextField nameText = new JTextField(8);
	private JTable programsTable, samplesTable;
	private JScrollPane programsTablePane, samplesTablePane;
	private JPanel programsFilesPanel = new JPanel();
	private JPanel samplesFilesPanel = new JPanel();
	private JPanel settingsPanel = new JPanel();
	private JPanel volumePanel = new JPanel();
	private JPanel sleepPanel = new JPanel();
	private JPanel defaultProgramPanel = new JPanel();
	private JPanel consolePanel = new JPanel();
	private JPanel monitorPanel = new JPanel();
	private JPanel controlPanel = new JPanel();
	private JPanel dataPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	private JPanel wifiPanel = new JPanel();
	private JPanel bluetoothPanel = new JPanel();
	private JTextArea theConsoleLog = new JTextArea(16, 68);
	private JTextArea theDataLog = new JTextArea(20, 68);
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
	private JButton runSampleButton = new JButton("Run sample");
	private JFormattedTextField freq = new JFormattedTextField(new Integer(500));
	private JFormattedTextField duration = new JFormattedTextField(new Integer(1000));
	private JComboBox<String> sensorList = new JComboBox<String>(sensorPorts);
	private JFormattedTextField txData = new JFormattedTextField();
	private JFormattedTextField rxDataLength = new JFormattedTextField(new Integer(1));
	private JFormattedTextField address;
	private JLabel rxData = new JLabel();
	private Border etchedBorder = BorderFactory.createEtchedBorder();
	private JButton soundButton = new JButton("Play Sound File");
	private JTextField newName = new JTextField(16);
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JLabel batteryValue = new JLabel("");
	private SensorPanel s1Panel = new SensorPanel(0);
	private SensorPanel s2Panel = new SensorPanel(1);
	private SensorPanel s3Panel = new SensorPanel(2);
	private SensorPanel s4Panel = new SensorPanel(3);
	
	// Other instance data
	private ExtendedFileModel fmPrograms, fmSamples;
	private int mv;
	private int rowLength = 8; // default
	private int recordCount;;
	private ConsoleViewComms cvc;
	private LCDDisplay lcd;
	private File directoryLastUsed;
	
	private RMIMenu menu;
    private RemoteEV3 ev3;
    private RMIRegulatedMotor motor0, motor1, motor2, motor3;
    
	// Formatter
	private static final NumberFormat FORMAT_FLOAT = NumberFormat.getNumberInstance();
	
	private String[] accessPoints = new String[0];

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
		cvc = new ConsoleViewComms(this,true);

		// Search Button: search for EV3s
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				search();
			}
		});

		// Connect Button: connect to selected EV£
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

		// Create the panels
		createEV3SelectionPanel();
		createConsolePanel();
		createDataPanel();
		createMonitorPanel();
		createControlPanel();
		createMiscellaneousPanel();
		createSettingsPanel();
		createWirelessPanel();

		// set the size of the files panel
		programsFilesPanel.setPreferredSize(filesPanelSize);
		samplesFilesPanel.setPreferredSize(filesPanelSize);
		
		// Set up the frame
		frame.setPreferredSize(frameSize);
		
		createRMITabs();
		
		frame.add(tabbedPane);
		frame.pack();
		frame.setVisible(true);
		return 0;
	}

	/**
	 * Get files from the EV3 and display them in the files panel
	 */
	private void showFiles() {
		// Layout and populate files table
		createProgramsTable("/home/root/lejos/samples/");
		createSamplesTable("/home/root/lejos/samples/");

		// Remove current content of files panel and recreate it
		programsFilesPanel.removeAll();
		samplesFilesPanel.removeAll();
		createFilesPanel();
		createSamplesPanel();
		
		// Recreate miscellaneous and settings panel
		otherPanel.removeAll();
		createMiscellaneousPanel();
		settingsPanel.removeAll();
		volumePanel.removeAll();
		sleepPanel.removeAll();
		defaultProgramPanel.removeAll();
		createSettingsPanel();
		
		// Process buttons

		// Delete Button: delete a file from the EV3
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

		// Upload Button: upload a file to the EV3
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				upload();
			}
		});

		// Download Button: download a file from from the EV3
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				download();
			}
		});

		// Run Button: run a program on the EV3
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				runProgram();
			}
		});
		
		// Run Button: run a program on the EV3
		runSampleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				runSample();
			}
		});

		// Set Name Button: set a new name for the EV3
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

		frame.revalidate();
	}

	/**
	 * Lay out EV3 Selection panel
	 */
	private void createEV3SelectionPanel() {
		JPanel ev3Panel = new JPanel();
		ev3TablePane = new JScrollPane(ev3Table);
		ev3TablePane.setPreferredSize(ev3TableSize);
		ev3Panel.add(new JScrollPane(ev3TablePane), BorderLayout.WEST);
		frame.getContentPane().add(ev3Panel, BorderLayout.NORTH);
		ev3Table.setPreferredScrollableViewportSize(ev3ButtonsPanelSize);
		JLabel nameLabel = new JLabel("Name: ");
		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(nameText);
		JPanel ev3ButtonPanel = new JPanel();
		ev3ButtonPanel.add(namePanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(searchButton);
		buttonPanel.add(connectButton);
		ev3ButtonPanel.add(buttonPanel);
		ev3ButtonPanel.setPreferredSize(ev3ButtonsPanelSize);
		ev3Panel.add(ev3ButtonPanel, BorderLayout.EAST);
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
        lcd.setMinimumSize(new Dimension(LCD_WIDTH, LCD_HEIGHT));
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
		final JComboBox<String> volumeList = new JComboBox<String>(volumeLevels);
		JLabel keyClickVolumeLabel = new JLabel("Key Click Volume:");
		final JComboBox<String> volumeList2 = new JComboBox<String>(volumeLevels);
		volumePanel.add(volumesLabel);
		volumeDropdownPanel.add(volumeLabel);
		volumeDropdownPanel.add(volumeList);
		volumePanel.add(volumeDropdownPanel);
		JPanel clickDropdownPanel = new JPanel();
		clickDropdownPanel.add(keyClickVolumeLabel);
		clickDropdownPanel.add(volumeList2);
		volumePanel.add(clickDropdownPanel);
		JPanel freqDropdownPanel = new JPanel();
		JLabel keyClickFrequencyLabel = new JLabel("Key Click Frequency:");
		final JComboBox<String> volumeList3 = new JComboBox<String>(frequencyLevels);
		freqDropdownPanel.add(keyClickFrequencyLabel);
		freqDropdownPanel.add(volumeList3);
		volumePanel.add(freqDropdownPanel);
		JPanel lenDropdownPanel = new JPanel();
		JLabel keyClickLengthLabel = new JLabel("Key Click Length:");
		final JComboBox<String> volumeList4 = new JComboBox<String>(lengthLevels);
		lenDropdownPanel.add(keyClickLengthLabel);
		lenDropdownPanel.add(volumeList4);
		volumePanel.add(lenDropdownPanel);
		volumePanel.setPreferredSize(volumePanelSize);
		
		if (menu != null) {
			try {
				int volume = Integer.parseInt(menu.getSetting(Sound.VOL_SETTING));
				volumeList.setSelectedIndex(volume/10);
				int keyClickVolume = Integer.parseInt(menu.getSetting(Button.VOL_SETTING));
				volumeList2.setSelectedIndex(keyClickVolume/10);
				int keyClickFrequency = Integer.parseInt(menu.getSetting(Button.FREQ_SETTING));
				volumeList3.setSelectedIndex((keyClickFrequency-1)/100);
				int keyClickLength = Integer.parseInt(menu.getSetting(Button.LEN_SETTING));
				volumeList4.setSelectedIndex((keyClickLength-1)/100);
			} catch(IOException e) {
				showMessage("Failed to get volume settings");
			}
		}
		
		JButton setButton = new JButton("Set");
		volumePanel.add(setButton);
		
		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					menu.setSetting(Sound.VOL_SETTING, "" + (volumeList.getSelectedIndex() * 10));
					menu.setSetting(Button.VOL_SETTING, "" + (volumeList2.getSelectedIndex() * 10));
					menu.setSetting(Button.FREQ_SETTING, "" + ((volumeList3.getSelectedIndex()+1) * 100));
					menu.setSetting(Button.LEN_SETTING, "" + ((volumeList4.getSelectedIndex()+1) * 100));
				} catch (RemoteException e1) {
					showMessage("Failed to set volume settings");
				}
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
		final JComboBox<String> sleepList = new JComboBox<String>(volumeLevels);
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
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		JLabel batteryLabel = new JLabel("Battery:");
		JPanel batteryPanel = new JPanel();
		batteryPanel.setBorder(etchedBorder);
		batteryPanel.add(batteryLabel);
		batteryPanel.add(batteryValue);
		leftPanel.add(batteryPanel);
		centerPanel.add(this.s1Panel);
		centerPanel.add(this.s2Panel);
		rightPanel.add(this.s3Panel);
		rightPanel.add(this.s4Panel);
		
		monitorPanel.add(leftPanel);
		monitorPanel.add(centerPanel);
		monitorPanel.add(rightPanel);
		monitorPanel.add(monitorUpdateButton);
	}
	
	public void createWirelessPanel() {
	    TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			public int getColumnCount() { return 1; }
	        public int getRowCount() { return accessPoints.length;}
	        public Object getValueAt(int row, int col) { return accessPoints[row]; }
	        public String getColumnName(int col) { return "Wireless Access Point"; }
	  
	    };
		final JTable wifiTable = new JTable(dataModel);
		
		wifiPanel.add(new JScrollPane(wifiTable));
		JButton scanButton = new JButton("Scan");
		wifiPanel.add(scanButton);
		JButton configButton = new JButton("Configure");
		wifiPanel.add(configButton);
		
		scanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (ev3 == null) return;
					String[] aps = ev3.getWifi().getAccessPointNames();
					for(String ap : aps) {
						System.out.println(ap);
					}
					
					accessPoints = aps;
					wifiTable.revalidate();
					
				} catch (Exception ioe) {
					showMessage("Failed to get list of Wifi access point");
				}
			}
		});
		
		configButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row= wifiTable.getSelectedRow();
					
					JPasswordField pf = new JPasswordField();
					int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

					if (okCxl == JOptionPane.OK_OPTION) {
					  String password = new String(pf.getPassword());
					  System.out.println("You entered: " + password);
					  System.out.println("ssis is " + (String) wifiTable.getValueAt(row, 0));
					  menu.configureWifi((String) wifiTable.getValueAt(row, 0), password);
					}
					
					if (row < 0) showMessage("Please select an access point");
				} catch (Exception ioe) {
					showMessage("Failed to configure wifi");
				}
			}
		});
	}

	/**
	 *  Create the tabs for RMI
	 */
	private void createRMITabs() {
		tabbedPane.removeAll();
		tabbedPane.addTab("Programs", programsFilesPanel);
		tabbedPane.addTab("Samples", samplesFilesPanel);
		tabbedPane.addTab("Console", consolePanel);
		tabbedPane.addTab("Settings", settingsPanel);
		tabbedPane.addTab("Sensors", monitorPanel);
		tabbedPane.addTab("Motors", controlPanel);
		tabbedPane.addTab("Wifi", wifiPanel);
		tabbedPane.addTab("Bluetooth", bluetoothPanel);
		tabbedPane.addTab("Tools", otherPanel);
	}
	
	/**
	 *  Set up the files panel
	 */
	private void createFilesPanel() {
		programsFilesPanel.add(programsTablePane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(deleteButton);
		buttonPanel.add(uploadButton);
		buttonPanel.add(downloadButton);
		buttonPanel.add(runButton);
		buttonPanel.add(soundButton);
		buttonPanel.add(formatButton);
		buttonPanel.add(setDefaultButton);
		buttonPanel.setPreferredSize(filesButtonsPanelSize);
		programsFilesPanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 *  Set up the files panel
	 */
	private void createSamplesPanel() {
		samplesFilesPanel.add(samplesTablePane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(runSampleButton);
		buttonPanel.setPreferredSize(filesButtonsPanelSize);
		samplesFilesPanel.add(buttonPanel, BorderLayout.SOUTH);
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
	 * Create rename EV3 panel
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
		String firmwareVersionString = "Unknown";
		String menuVersionString = "Unknown";
		
		if (menu != null) {
			try {
				firmwareVersionString = menu.getVersion();
				menuVersionString = menu.getMenuVersion();
			} catch (IOException ioe) {
				showMessage("IO Exception getting device information");
			}
		}
		
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
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(txDataSend);
		i2cPanel.add(buttonsPanel);
		i2cPanel.setBorder(BorderFactory.createEtchedBorder());
		i2cPanel.setPreferredSize(i2cPanelSize);
		otherPanel.add(i2cPanel);
		
		txDataSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				i2cSend();
			}
		});
		
	}

	/**
	 *  Set up the files table
	 */
	private void createProgramsTable(String directory) {
		fmPrograms = new ExtendedFileModel(menu, PROGRAMS_DIR, true);
		String[] programs;
		try {
			programs = menu.getProgramNames();
			long[] sizes = new long[programs.length];
			for(int i=0;i<sizes.length;i++) {
				sizes[i] = menu.getFileSize(directory + programs[i]);
			}
			fmPrograms.fetchFiles();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		programsTable = new JTable(fmPrograms);
		programsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		programsTable.getColumnModel().getColumn(0).setPreferredWidth(fileNameColumnWidth);
		programsTablePane = new JScrollPane(programsTable);
		programsTablePane.setPreferredSize(filesAreaSize);
		
		// Change first parameter to System.out to enable debugging
        new FileDrop( null, programsTablePane, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   
            		String fileName = files[i].getName();
            		int row = fmPrograms.getRow(fileName);
            		try {
            			if (row >= 0) fmPrograms.delete(fileName, row);
            		} catch (IOException e) {
            			showMessage("IOException deleting file");
            		}
                	uploadFile(files[i]);
                }
            }
        }); 
	}
	
	/**
	 *  Set up the files table
	 */
	private void createSamplesTable(String directory) {
		fmSamples = new ExtendedFileModel(menu, SAMPLES_DIR, false);
		String[] programs;
		try {
			programs = menu.getSampleNames();
			long[] sizes = new long[programs.length];
			for(int i=0;i<sizes.length;i++) {
				sizes[i] = menu.getFileSize(directory + programs[i]);
			}
			fmSamples.fetchFiles();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		samplesTable = new JTable(fmSamples);
		samplesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		samplesTable.getColumnModel().getColumn(0).setPreferredWidth(fileNameColumnWidth);
		samplesTablePane = new JScrollPane(samplesTable);
	    samplesTablePane.setPreferredSize(filesAreaSize);
		
		// Change first parameter to System.out to enable debugging
        new FileDrop( null, samplesTablePane, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   
            		String fileName = files[i].getName();
            		int row = fmSamples.getRow(fileName);
            		try {
            			if (row >= 0) fmSamples.delete(fileName, row);
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
	 * Download a file from the EV3
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
		if (ev3 == null) return;
		batteryValue.setText("" + mv);
		s1Panel.update();
		s2Panel.update();
		s3Panel.update();
		s4Panel.update();
	}

	/**
	 * Clear the files tab.
	 */
	private void clearFiles() {
		programsFilesPanel.removeAll();
		programsFilesPanel.repaint();
		samplesFilesPanel.removeAll();
		samplesFilesPanel.repaint();
	}

	/**
	 * Switch between EV3s in table of available EV3s
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			int row = ev3Table.getSelectedRow();
			if (row < 0) return;		
			
		}
	}

	/**
	 * Search for available EV3s and populate table with results.
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
		s1Panel.close();
		s2Panel.close();
		s3Panel.close();
		s4Panel.close();
		if (cvc != null) cvc.close();
	}

	/**
	 * Toggle Connect button between Connect and Disconnect
	 */
	private void updateConnectButton(boolean connected) {
		connectButton.setText((connected ? "Disconnect" : "Connect"));
	}

	/**
	 * Stop the motors on the EV3 and update the tachometer values
	 */
	private void stopMotors() {
		try {
			if (motor0 != null) {
				motor0.stop(true);
				motor0.close();
				motor0=null;
			}
			if (motor1 != null) {
				motor1.stop(true);
				motor1.close();
				motor1=null;
			}
			if (motor2 != null) {
				motor2.stop(true);
				motor2.close();
				motor2=null;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
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
	 * Retrieve the sensor and battery values from the EV3
	 */
	private void getSensorValues() {
		if (ev3 != null) {
			mv = ev3.getBattery().getVoltageMilliVolt();
		}
	}

	/**
	 * Connect to the EV3
	 */
	private void connect() {
		String name = nameText.getText();
		if (name != null && name.length() > 0) {
			System.out.println("Connecting to " + name);
			try {
				menu = (RMIMenu) Naming.lookup("//" + name + "/RemoteMenu");
				ev3 = new RemoteEV3(name);
				s1Panel.setEV3(ev3);
				s2Panel.setEV3(ev3);
				s3Panel.setEV3(ev3);
				s4Panel.setEV3(ev3);
				cvc.connectTo(name, name, 0, true);
				showFiles();
			} catch (RemoteException | MalformedURLException
					| NotBoundException e) {
				e.printStackTrace();
			}
		}	
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
			for (int i = 0; i < fmPrograms.getRowCount(); i++) {
				Boolean b = (Boolean) fmPrograms.getValueAt(i,ExtendedFileModel.COL_DELETE);
				String fileName = (String) fmPrograms.getValueAt(i,ExtendedFileModel.COL_NAME);
				boolean deleteIt = b.booleanValue();
				if (deleteIt) {
					System.out.println("Deleting " + fileName);
					menu.deleteFile(fileName);
				}
			}
			fmPrograms.fetchFiles();
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
		    System.out.println("Uploading " + file.getName());
			menu.uploadFile("/home/lejos/programs/" + file.getName(), data);
		    in.close();
			fmPrograms.fetchFiles();
		} catch (IOException ioe) {
			showMessage("IOException uploading file");
		}
		frame.setCursor(normalCursor);
	}
	
	/**
	 * Download the selected file
	 */
	private void download() {
		int row = programsTable.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fmPrograms.getFile(row).fileName;
		int size = fmPrograms.getFile(row).fileSize;
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
	private void runProgram() {
		int row = programsTable.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		String name = fmPrograms.getFile(row).fileName;
		
		System.out.println("Running " + name);
		try {
			menu.runProgram(name.replaceFirst(".jar", ""));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Run the selected file.
	 */
	private void runSample() {
		int row = samplesTable.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		String name = fmSamples.getFile(row).fileName;
		
		System.out.println("Running Sample  " + name);
		try {
			menu.runSample(name.replaceFirst(".jar", ""));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Change the friendly name of the EV3
	 */
	private void rename(String name) {
	}
	
	/**
	 * Move the motors
	 */
	private void move(int speed0, int speed1, int speed2 ) {
		int[] lim = getLimits();
		
		try {
			if (ev3 == null) return;
			if (selectors[0].isSelected()) {
				motor0 = ev3.createRegulatedProvider("A");
			    motor0.setSpeed(speed0);
			    if (lim[0] !=0) motor0.rotateTo(lim[0]);
			    else if (speed0 > 0) motor0.forward(); else motor0.backward();
			}
			if (selectors[1].isSelected()) {
				motor1 = ev3.createRegulatedProvider("B");
			    motor1.setSpeed(speed1);
			    if (lim[1] !=0) motor1.rotateTo(lim[1]);
			    else if (speed1 > 0) motor1.forward(); else motor1.backward();
			}
			if (selectors[2].isSelected()) {
				motor2 = ev3.createRegulatedProvider("C");
			    motor2.setSpeed(speed2);
			    if (lim[2] !=0) motor2.rotateTo(lim[2]);
			    else if (speed2 > 0) motor2.forward(); else motor2.backward();
			}
			if (selectors[3].isSelected()) {
				motor3 = ev3.createRegulatedProvider("D");
			    motor3.setSpeed(speed2);
			    if (lim[2] !=0) motor3.rotateTo(lim[2]);
			    else if (speed2 > 0) motor3.forward(); else motor3.backward();
			}
		} catch (IOException ioe) {
			showMessage("IOException updating control");
		}
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
		RMIRegulatedMotor motor;
		
		if (ev3 == null) return;
		
		if (b == resetButtons[0]) motor = motor0;
		if (b == resetButtons[1]) motor = motor1;
		if (b == resetButtons[2]) motor = motor2;
		if (b == resetButtons[3]) motor = motor3;
		
		
		try {
			// TODO: add tacho stuff to RMIRegulatedMotor
		} catch (Exception ioe) {
			showMessage("IO Exception resetting motor");
		}
	}
	
	/**
	 * Play a sound file
	 */
	private void playSoundFile() {
		int row = programsTable.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fmPrograms.getFile(row).fileName;
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
		int addr= ((Number)address.getValue()).intValue(); 
		
		try {
			Port p = ev3.getPort((String) sensorList.getSelectedItem()); 
			
			I2CPort i2c = p.open(RemoteI2CPort.class);
			
			int rLen = ((Number) rxDataLength.getValue()).intValue();
			byte[] reply = new byte[rLen];
			byte[] data = fromHex(txData.getText());
			
			i2c.i2cTransaction(addr, data, 0, data.length, reply, 0, rLen);
			
			if (rLen > 0) {
				String hex = toHex(reply);
				rxData.setText(hex);
			} else
				rxData.setText("null");
			
			i2c.close();
			
		} catch (Exception e) {
			showMessage("IO Exception sending txData: " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert a byte array to a string of hex characters
	 */
	private String toHex(byte[] b) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			if (i > 0)
				output.append(' ');
			byte j = b[i];
			output.append(Character.forDigit((j >> 4) & 0xF, 16));
			output.append(Character.forDigit(j & 0xF, 16));
		}
		return output.toString();
	}

	
	/**
	 * Convert a string of hex characters to a byte array
	 */
	private byte[] fromHex(String s) {
		byte[] reply = new byte[s.length() / 2];
		for (int i = 0; i < reply.length; i++) {
			char c1 = s.charAt(i * 2);
			char c2 = s.charAt(i * 2 + 1);
			reply[i] = (byte) (getHexDigit(c1) << 4 | getHexDigit(c2));
		}
		return reply;
	}
	
	/**
	 * Convert a character to a hex digit
	 */
	private int getHexDigit(char c) {
		if (c >= '0' && c <= '9') return c - '0';
		if (c >= 'a' && c <= 'f') return c - 'a' + 10;
		if (c >= 'A' && c <= 'F') return c - 'A' + 10;
		return 0;
	}
	
	/**
	 * Format the file system
	 */
	private void format() {
		try {
			menu.deleteAllPrograms();
			fmPrograms.fetchFiles();
		} catch (Exception ioe) {
			showMessage("IO Exception formatting file system");
		}
	}
	
	private void noFileSelected() {
		showMessage("No file selected");
	}
	
	private void setDefaultProgram() {
		int row = programsTable.getSelectedRow();
		if (row < 0) {
			noFileSelected();
			return;
		}
		
		String fileName = fmPrograms.getFile(row).fileName;
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
