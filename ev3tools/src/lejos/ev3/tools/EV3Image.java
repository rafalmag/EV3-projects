/*
 * Convert and image into a form suitable for use with the leJOS graphics
 * classes.
 *
 * Original code by Programus, imported to leJOS by Andy
 */
package lejos.ev3.tools;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class EV3Image {

	public static void main(String[] args)
	{
		ToolStarter.startSwingTool(EV3Image.class, args);
	}
	
	public static int start(String[] args)
	{
		final JFrame frame = new JFrame("leJOS EV3 Image Converter");
		EV3ImageMainPanel panel = new EV3ImageMainPanel();
		frame.getContentPane().add(panel);
		frame.setJMenuBar(panel.getMenuBar(panel));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(500, 300);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
		return 0;
	}

}
