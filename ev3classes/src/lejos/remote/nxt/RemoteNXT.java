package lejos.remote.nxt;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Battery;
import lejos.hardware.port.Port;
import lejos.internal.ev3.EV3Port;

public class RemoteNXT implements NXT {
	
	private NXTCommand nxtCommand;
	private NXTComm nxtComm;
	private Battery battery;
	private String name;
	private byte[] address = new byte[6];
	
    protected ArrayList<RemoteNXTPort> ports = new ArrayList<RemoteNXTPort>();
    
    private void createPorts()
    {
        // Create the port objects
        ports.add(new RemoteNXTPort("S1", RemoteNXTPort.SENSOR_PORT, 0, nxtCommand));
        ports.add(new RemoteNXTPort("S2", RemoteNXTPort.SENSOR_PORT, 1, nxtCommand));
        ports.add(new RemoteNXTPort("S3", EV3Port.SENSOR_PORT, 2, nxtCommand));
        ports.add(new RemoteNXTPort("S4", RemoteNXTPort.SENSOR_PORT, 3, nxtCommand));
        ports.add(new RemoteNXTPort("A", RemoteNXTPort.MOTOR_PORT, 0, nxtCommand));
        ports.add(new RemoteNXTPort("B", RemoteNXTPort.MOTOR_PORT, 1, nxtCommand));
        ports.add(new RemoteNXTPort("C", RemoteNXTPort.MOTOR_PORT, 2, nxtCommand));
    }
	
	private RemoteNXT(String name, NXTCommConnector connector) throws IOException {		
		this.name = name;
		connect(connector);
		createPorts();
	}
	
	public RemoteNXT(String name, byte[] address) {
		createPorts();
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
        for(RemoteNXTPort p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
	}

	@Override
	public Battery getBattery() {
		return battery;
	}

}
