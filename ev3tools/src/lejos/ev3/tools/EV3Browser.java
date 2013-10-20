package lejos.ev3.tools;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import lejos.remote.ev3.RMIMenu;

/**
 * 
 * Graphical file browser for leJOS NXJ. Supports uploading,, downloading, and
 * deleting files. Also supports running programs, and setting the name of the
 * NXT.
 * 
 * @author Lawrie Griffiths <lawrie.griffiths@ntlworld.com>
 */
public class EV3Browser {
	
	private static final String PROGRAMS_DIR = "/home/lejos/programs/";
	private static final String SAMPLES_DIR = "/home/root/lejos/samples/";

	public static final String remoteHost = "//192.168.0.9/";
	public static final int MAX_FILES = 30;
	private Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static String title = "EV3 File Browser";
	private JFrame frame;
	private RMIMenu menu;

	public static void main(String args[]) {
		ToolStarter.startSwingTool(EV3Browser.class, args);
	}

	public static int start(String[] args) throws Exception {
		return new EV3Browser().run(args);
	}

	private int run(String[] args) throws Exception {

		frame = new JFrame(title);
		menu = (RMIMenu) Naming.lookup(remoteHost + "RemoteMenu");

		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				System.exit(0);
			}
		};
		frame.addWindowListener(listener);

		showFiles(frame);

		return 0;

	}

	private void showFiles(final JFrame frame) throws RemoteException {

		frame.setTitle(title + " : ");

		final ExtendedFileModel fm = new ExtendedFileModel(menu, SAMPLES_DIR, false);
		fm.fetchFiles();

		final JTable table = new JTable(fm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(450);

		final JScrollPane tablePane = new JScrollPane(table);
		tablePane.setPreferredSize(new Dimension(605, 500));

		frame.getContentPane().add(tablePane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton deleteButton = new JButton("Delete Files");
		JButton uploadButton = new JButton("Upload file");
		JButton downloadButton = new JButton("Download file");
		JButton runButton = new JButton("Run program");
		JButton setDefaultButton = new JButton("Set as default");
		JButton nameButton = new JButton("Set Name");

		buttonPanel.add(deleteButton);
		buttonPanel.add(uploadButton);
		buttonPanel.add(downloadButton);
		buttonPanel.add(runButton);
		buttonPanel.add(setDefaultButton);
		buttonPanel.add(nameButton);

		frame.getContentPane().add(new JScrollPane(buttonPanel),
				BorderLayout.SOUTH);

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					deleteFile(frame, fm);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});

		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				uploadFile(frame, fm);
			}
		});

		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int i = table.getSelectedRow();
				downloadFile(frame, fm, i);
			}
		});

		setDefaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int i = table.getSelectedRow();
				String fileName = fm.getFile(i).fileName;
				setDefault(fileName);
			}
		});

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int i = table.getSelectedRow();
				runFile(fm, i);
			}
		});

		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeName(frame);
			}
		});

		frame.pack();
		frame.setVisible(true);
	}

	public void getFile(File file, String fileName, int size) {
	}

	public void runProgram(String fileName) throws IOException {
		menu.runProgram(fileName);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}

	private void deleteFile(final JFrame frame, final ExtendedFileModel fm)
			throws RemoteException {
		frame.setCursor(hourglassCursor);

		for (int i = 0; i < fm.getRowCount(); i++) {
			Boolean b = (Boolean) fm
					.getValueAt(i, ExtendedFileModel.COL_DELETE);
			String fileName = (String) fm.getValueAt(i,
					ExtendedFileModel.COL_NAME);
			boolean deleteIt = b.booleanValue();
			if (deleteIt) {
				// System.out.println("Deleting " + fileName);
				menu.deleteFile(fileName);
			}
		}
		fm.fetchFiles();
		frame.setCursor(normalCursor);
	}

	private void uploadFile(final JFrame frame, final ExtendedFileModel fm) {
		JFileChooser fc = new JFileChooser();

		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			frame.setCursor(hourglassCursor);
			File file = fc.getSelectedFile();
			frame.setCursor(normalCursor);
		}
	}

	private void downloadFile(final JFrame frame, final ExtendedFileModel fm,
			int i) {
		if (i < 0)
			return;
		String fileName = fm.getFile(i).fileName;
		int size = fm.getFile(i).fileSize;
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

	private void setDefault(String name) {
		frame.setCursor(hourglassCursor);
		frame.setCursor(normalCursor);
	}

	private void runFile(final ExtendedFileModel fm, int i) {
		if (i < 0)
			return;
		String fileName = fm.getFile(i).fileName;
		try {
			runProgram(fileName);
			System.exit(0);
		} catch (IOException ioe) {
			showMessage("IOException running program");
		}
	}

	private void changeName(final JFrame frame) {
		String name = JOptionPane.showInputDialog(frame, "New Name");

		if (name != null && name.length() <= 16) {
			frame.setCursor(hourglassCursor);
			// nxtCommand.setFriendlyName(name);
			frame.setTitle(title + " : " + name);
			frame.setCursor(normalCursor);
		}
	}
}
