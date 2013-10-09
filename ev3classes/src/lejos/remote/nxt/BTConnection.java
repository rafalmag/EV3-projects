package lejos.remote.nxt;

import java.io.IOException;

import lejos.internal.io.NativeSocket;

public class BTConnection extends NXTConnection {
	NativeSocket socket;
	int mode;
	byte[] header = new byte[2];
	byte[] buffer = new byte[256];
	
	BTConnection(NativeSocket socket, int mode) {
		this.socket = socket;
		this.mode = mode;
		System.out.println("Mode is " + mode);
	}

	@Override
	public void close() throws IOException {
		socket.close();	
	}

	// TODO : Need robust versions of read and write
	@Override
	public int read(byte[] buf, int length) {
		System.out.println("Reading ...");
		if (mode != NXTConnection.RAW) {
			int len = socket.read(buffer, length);
			System.out.println("Read " + len + " bytes");
			if (len > 2) {
				for(int i=0;i<len-2;i++) buf[i] = buffer[i+2];
				return len-2;
			} else return len;
		} else return socket.read(buf, length);
	}

	@Override
	public int write(byte[] buffer, int numBytes) {
		System.out.println("Sending " + numBytes + " bytes");
		
		if (mode != NXTConnection.RAW) {
			System.out.println("Sending header");
			header[0] = (byte) numBytes;
			header[1] = (byte) (numBytes >> 8);
			socket.write(header,0,2);
		}
		socket.write(buffer, 0, numBytes);
		return 0;
	}

	@Override
	public int read(byte[] buf, int length, boolean b) {
		System.out.println("Reading  with wait = " + b);
		if (mode != NXTConnection.RAW) {
			int len = socket.read(buffer, length);
			System.out.println("Read " + len + " bytes");
			if (len > 2) {
				for(int i=0;i<len-2;i++) buf[i] = buffer[i+2];
				return len-2;
			} else return len;
		} else return socket.read(buf, length);
	}
	
}
