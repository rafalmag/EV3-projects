package pl.rafalmag.ev3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.utility.Delay;

public class SoundMock {

	private static final int RIFF_HDR_SIZE = 44;
	private static final int RIFF_RIFF_SIG = 0x52494646;
	private static final int RIFF_WAVE_SIG = 0x57415645;
	private static final int RIFF_FMT_SIG = 0x666d7420;
	private static final short RIFF_FMT_PCM = 0x0100;
	private static final short RIFF_FMT_1CHAN = 0x0100;
	private static final short RIFF_FMT_8BITS = 0x0800;
	private static final int RIFF_DATA_SIG = 0x64617461;

	private static final int PCM_BUFFER_SIZE = 250;

	public static final int VOL_MAX = 100;
	public static final String VOL_SETTING = "lejos.volume";
	// Instruments (yes I know they don't sound anything like the names!)
	public final static int[] PIANO = new int[] { 4, 25, 500, 7000, 5 };
	public final static int[] FLUTE = new int[] { 10, 25, 2000, 1000, 25 };
	public final static int[] XYLOPHONE = new int[] { 1, 8, 3000, 5000, 5 };

	private static int masterVolume = 0;

	private final NativeDeviceMock dev;

	private static final byte OP_BREAK = 0;
	private static final byte OP_TONE = 1;
	private static final byte OP_PLAY = 2;
	private static final byte OP_REPEAT = 3;
	private static final byte OP_SERVICE = 4;

	public SoundMock(NativeDeviceMock dev) {
		this.dev = dev;
	}

	/**
	 * Play a wav file
	 * 
	 * @param file
	 *            the 8-bit PWM (WAV) sample file
	 * @param vol
	 *            the volume percentage 0 - 100
	 * @return The number of milliseconds the sample will play for or < 0 if
	 *         there is an error.
	 */
	public int playSample(InputStream is, int vol) {
		int offset = 0;
		int sampleRate = 0;
		int dataLen = 0;
		// Now check for a RIFF header
		try (DataInputStream d = new DataInputStream(is)) {
			// First check that we have a wave file. File must be at least 44
			// bytes
			// in size to contain a RIFF header.
			if (d.available() < RIFF_HDR_SIZE)
				return -9;
			if (d.readInt() != RIFF_RIFF_SIG)
				return -1;
			// Skip chunk size
			d.readInt();
			// Check we have a wave file
			if (d.readInt() != RIFF_WAVE_SIG)
				return -2;
			if (d.readInt() != RIFF_FMT_SIG)
				return -3;
			offset += 16;
			// Now check that the format is PCM, Mono 8 bits. Note that these
			// values are stored little endian.
			int sz = readLSBInt(d);
			if (d.readShort() != RIFF_FMT_PCM)
				return -4;
			if (d.readShort() != RIFF_FMT_1CHAN)
				return -5;
			sampleRate = readLSBInt(d);
			d.readInt();
			d.readShort();
			if (d.readShort() != RIFF_FMT_8BITS)
				return -6;
			// Skip any data in this chunk after the 16 bytes above
			sz -= 16;
			offset += 20 + sz;
			while (sz-- > 0)
				d.readByte();
			// Skip optional chunks until we find a data sig (or we hit eof!)
			for (;;) {
				int chunk = d.readInt();
				dataLen = readLSBInt(d);
				offset += 8;
				if (chunk == RIFF_DATA_SIG)
					break;
				// Skip to the start of the next chunk
				offset += dataLen;
				while (dataLen-- > 0)
					d.readByte();
			}
			if (vol >= 0)
				vol = (vol * masterVolume) / 100;
			else
				vol = -vol;
			byte[] buf = new byte[PCM_BUFFER_SIZE * 4 + 1];
			// get ready to play, set the volume
			buf[0] = OP_PLAY;
			// buf[1] = (byte)((vol*8)/100);
			buf[1] = (byte) vol;
			dev.write(buf, 2);
			// now play the file
			buf[1] = 0;
			while ((dataLen = d.read(buf, 1, buf.length - 1)) > 0) {
				// now make sure we write all of the data
				offset = 0;
				while (offset < dataLen) {
					buf[offset] = OP_SERVICE;
					int len = dataLen - offset;
					if (len > PCM_BUFFER_SIZE)
						len = PCM_BUFFER_SIZE;
					int written = dev.write(buf, offset, len + 1);
					if (written == 0) {
						Delay.msDelay(1);
					} else
						offset += written;
				}
			}
		} catch (IOException e) {
			return -1;
		}

		// playSample(file.getPage(), offset, dataLen, sampleRate, vol);
		return 0;
	}

	/**
	 * Read an LSB format
	 * 
	 * @param d
	 *            stream to read from
	 * @return the read int
	 * @throws java.io.IOException
	 */
	private static int readLSBInt(DataInputStream d) throws IOException {
		int val = d.readByte() & 0xff;
		val |= (d.readByte() & 0xff) << 8;
		val |= (d.readByte() & 0xff) << 16;
		val |= (d.readByte() & 0xff) << 24;
		return val;
	}

}
