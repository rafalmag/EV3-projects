package lejos.remote.nxt;

import java.io.IOException;

import lejos.hardware.Battery;
import lejos.hardware.port.Port;

public class RemoteNXT implements NXT {
	
	private NXTCommand nxtCommand;
	private NXTComm nxtComm;
	private Battery battery;
	private String name;
	private byte[] address = new byte[6];
	
	private RemoteNXT(String name, NXTCommConnector connector) throws IOException {
		this.name = name;
		connect(connector);
	}
	
	public RemoteNXT(String name, byte[] address) {
		this.name = name;
		this.address = address;
	}
	
	public void connect(NXTCommConnector connector) throws IOException {
        nxtComm = new NXTComm(connector);
        System.out.println("Connecting to " + name);
		boolean open = nxtComm.open(name, NXTConnection.LCP);
		if (!open) throw new IOException("Failed to connect to " + name);
		System.out.println("Connected");;
		nxtCommand = new NXTCommand(nxtComm);
		System.out.println("Creating remote battery");
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
