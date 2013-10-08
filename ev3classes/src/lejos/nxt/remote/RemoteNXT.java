package lejos.nxt.remote;

import java.io.IOException;

import lejos.hardware.Battery;
import lejos.hardware.port.Port;

public class RemoteNXT implements NXT {
	
	private NXTCommand nxtCommand;
	private NXTComm nxtComm;
	private Battery battery;
	
	private RemoteNXT(String name, NXTCommConnector connector) throws IOException {
        nxtComm = new NXTComm(connector);
		boolean open = nxtComm.open(name, NXTConnection.LCP);
		if (!open) throw new IOException("Failed to connect to " + name);
		nxtCommand = new NXTCommand(nxtComm);
		battery = new RemoteBattery(nxtCommand);
	}
	
	public static NXT get(String name, NXTCommConnector connector) throws IOException {
		return new RemoteNXT(name, connector);
	}

	@Override
	public Port getPort(String portName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Battery getBattery() {
		return battery;
	}

}
