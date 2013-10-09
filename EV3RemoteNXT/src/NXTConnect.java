import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;


public class NXTConnect {
	private static final String NXT = "00:16:53:12:92:AA";

	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();	
		System.out.println("Connecting to " + NXT);
		NXTConnection connection = connector.connect(NXT, NXTConnection.PACKET);
		System.out.println("Connected");
		
		DataInputStream input = connection.openDataInputStream();
		DataOutputStream output = connection.openDataOutputStream();
		
		System.out.println("Sending data");
				
		for(int i=0;i<100;i++) {
			output.writeInt(i);
			output.flush();
			System.out.println("Read: " + input.readInt());
		}
		
		System.out.println("All data sent");
		
		output.close();
		input.close();
		connection.close();	
		
		System.out.println("Connection closed");
	}
}
