package lejos.ev3.startup;

import java.io.IOException;
import java.io.OutputStream;

public class SynchronizedOutputStream extends OutputStream {
	private OutputStream os;
	
	public SynchronizedOutputStream(OutputStream os) {
		this.os = os;
	}
	
	@Override
	public synchronized void write(int b) throws IOException {
		os.write(b);
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len)  throws IOException {
		//System.err.println("Writing " + len + " bytes with offset");
		os.write(b, off, len);
		os.flush();
	}
	
	@Override
	public synchronized void write(byte[] b)  throws IOException {
		//System.err.println("Writing " + b.length + " bytes");
		os.write(b);
	}
	
	@Override
	public synchronized void close()  throws IOException {
		os.close();
	}
	
	@Override
	public synchronized void flush() throws IOException {
		os.flush();
	}
	
	public synchronized void writeLCD(byte[] b) throws IOException {
		//System.err.println("Writing LCD screen " + b.length + " bytes");
		os.write((int) 255);
		os.write((int) 0);
		os.write(b);
		os.flush();
		//System.err.println("LCD finished");
	}

}
