package lejos.remote.nxt;

import lejos.internal.io.NativeHCI;
import lejos.internal.io.NativeSocket;

import com.sun.jna.LastErrorException;

public class BTConnector extends NXTCommConnector  {
	NativeSocket socket = new NativeSocket(NativeHCI.AF_BLUETOOTH, NativeHCI.SOCK_STREAM, NativeHCI.BTPROTO_RFCOMM);
	
	public BTConnection connect(String target, int mode) throws LastErrorException {
		NativeSocket.SockAddr sa = new NativeSocket.SockAddr();
		
		for(int i=0;i<6;i++) {
			byte b = (byte) (int) Integer.valueOf(target.substring(i*3,i*3+2), 16);
			sa.bd_addr[5-i] = b;
		}
		
		/*for(int i=0;i<6;i++) {
			System.out.print(Integer.toHexString(sa.bd_addr[i] & 0xFF)  + ":");
		}
		System.out.println();*/
		
		socket.connect(sa, sa.size());
		
		return new BTConnection(socket, mode);
	}

	@Override
	public NXTConnection waitForConnection(int timeout, int mode) {
		NativeSocket.SockAddr sa = new NativeSocket.SockAddr();
		
		socket.bind(sa, sa.size());
		
		socket.listen(1);
		
		NativeSocket client = socket.accept();
		return new BTConnection(client, mode);
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}
}
