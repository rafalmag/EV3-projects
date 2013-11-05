import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class NXTReceive {

	public static void main(String[] args) throws Exception {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		
		System.out.println("Waiting for connection ...");
		NXTConnection con = connector.waitForConnection(0, NXTConnection.RAW);
		System.out.println("Connected");
		
        DataInputStream dis = con.openDataInputStream();
        DataOutputStream dos = con.openDataOutputStream();

        while(true)
        {
            int n;
            try{
                n = dis.readInt();
            } catch (EOFException e)
            {
                break;
            }
            System.out.println("Read " + n);
            dos.writeInt(-n);
            dos.flush();
        }
        
        Delay.msDelay(1000);

        dis.close();
        dos.close();
        con.close();
		
	}
}
