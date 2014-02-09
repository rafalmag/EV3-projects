package pl.rafalmag.ev3;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import lejos.hardware.Sound;

import org.junit.Ignore;
import org.junit.Test;

import pl.rafalmag.ev3.clock.Cuckoo;

@Ignore
public class SoundFileTest {

	@Test
	public void should_play_file() throws Exception {
		// given
		try (InputStream is = getClass().getResourceAsStream(Cuckoo.CUCKOO_WAV)) {
			if (is == null) {
				throw new IOException("Cannot find wav=" + Cuckoo.CUCKOO_WAV);
			}
			SoundMock soundMock = new SoundMock(new NativeDeviceMock());
			// when
			int errorCode = soundMock.playSample(is, Sound.VOL_MAX);
			// then
			assertThat(errorCode, equalTo(0));
		}
	}
}
