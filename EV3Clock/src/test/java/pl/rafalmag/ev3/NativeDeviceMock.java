package pl.rafalmag.ev3;

public class NativeDeviceMock {

	public int write(byte[] buf, int offset, int len) {
		return len;
	}

	public void write(byte[] buf, int len) {

	}

}
