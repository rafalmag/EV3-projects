package lejos.ev3.tools;

import java.io.IOException;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

/**
 * Console output monitor class.
 * This class provides access to console output from a NXT program. The program
 * simply writes strings using the NXT RConsole class. These are sent to the
 * PC via the USB (or Bluetooth) connection.
 *
 */ 
public class EV3Console implements ConsoleViewerUI {

	public static void main(String[] args)
	{
		ToolStarter.startTool(EV3Console.class, args);
	}
	
	public static int start(String[] args) throws IOException
	{
		return new EV3Console().run(args);
	}


	private int run(String[] args) throws IOException
	{
		ConsoleCommandLineParser fParser = new ConsoleCommandLineParser(EV3Console.class, "[options]");
		CommandLine commandLine;
		try
		{
			commandLine = fParser.parse(args);
		}
		catch (ParseException e)
		{
			fParser.printHelp(System.err, e);
			return 1;
		}
		
		if (commandLine.hasOption("h"))
		{
			fParser.printHelp(System.out);
			return 0;
		}
		
		int protocols = 0;
		boolean blueTooth = commandLine.hasOption("b");
		boolean usb = commandLine.hasOption("u");
		String name = AbstractCommandLineParser.getLastOptVal(commandLine, "n");
		String address = AbstractCommandLineParser.getLastOptVal(commandLine, "d");
        String debugFile = AbstractCommandLineParser.getLastOptVal(commandLine, "di");

		ConsoleViewComms comm = new ConsoleViewComms(this, false);
		boolean connected = comm.connectTo(name, address, protocols, false);
		if (!connected) {
			logMessage("Failed to connect to NXT");
			return 1;
		}
        comm.waitComplete();
		return 0;
	}

	public void append(String value) {
		System.out.print(value);
	}
    
    public void updateLCD(byte[] buffer) {
    }

	public void connectedTo(String name, String address) {
	}

	public void logMessage(String msg) {
		System.out.println(msg);		
	}

	public void setStatus(String msg) {
	}
}
