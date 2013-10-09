package lejos.remote.nxt;
import java.io.IOException;
import java.nio.ByteBuffer;

import lejos.internal.io.NativeSocket;

public class BTConnection extends NXTConnection {
	NativeSocket socket;
	ByteBuffer buf = ByteBuffer.allocate(256);
	
	BTConnection(NativeSocket socket) {
		this.socket = socket;
	}

	@Override
	public void close() throws IOException {
		socket.close();	
	}

	@Override
	public int read(byte[] buf, int length) {
		return socket.read(buf, length);
	}

	@Override
	public int write(byte[] buffer, int numBytes) {
		socket.write(buffer, 0, numBytes);
		return 0;
	}

	@Override
	public int read(byte[] buf, int length, boolean b) {
		return socket.read(buf, length);
	}
	
}
