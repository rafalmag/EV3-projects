package pl.rafalmag.ev3;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TimeAngleUtilsTest {

	// TODO clock to angle
	@Parameters
	@Test
	public void should_convert_clock_to_angle(Time time, int expectedAngle)
			throws Exception {
		// given
		// when
		int angle = TimeAngleUtils.toAngle(time);

		// then
		assertThat(angle).isEqualTo(expectedAngle);
	}

	Object[] parametersForShould_convert_clock_to_angle() {
		return $($(new Time(00, 00), 0), $(new Time(01, 00), 360),
				$(new Time(00, 30), 180), $(new Time(11, 00), 360 * 11),
				$(new Time(12, 00), 0), $(new Time(13, 00), 360),
				$(new Time(12, 30), 180), $(new Time(23, 00), 360 * 11),
				$(new Time(23, 30), 360 * 11 + 180));
	}

	@Parameters
	@Test
	public void should_convert_angle_to_time(int angle, Time expectedTime)
			throws Exception {
		// given
		// when
		Time time = TimeAngleUtils.toTime(angle);

		// then
		assertThat(time).isEqualTo(expectedTime);
	}

	Object[] parametersForShould_convert_angle_to_time() {
		return $($(0, new Time(00, 00)), $(180, new Time(00, 30)),
				$(360, new Time(01, 00)), $(360 * 11, new Time(11, 00)),
				$(-180, new Time(11, 30)), $(48 * 360, new Time(00, 00)));
	}

	// TODO angle to clock !? - do I need that ?
	// TODO angle diff relative
	@Parameters
	@Test
	public void should_return_diff_angle_beetween_two_times(Time baseTime,
			Time givenTime, int expectedDiff) throws Exception {
		// given
		// when
		int angle = TimeAngleUtils.getDiffAngle(baseTime, givenTime);

		// then
		assertThat(angle).isEqualTo(expectedDiff);
	}

	Object[] parametersForShould_return_diff_angle_beetween_two_times() {
		return $(
				$(new Time(00, 00), new Time(00, 00), +0),
				$(new Time(00, 00), new Time(01, 00), +360),
				$(new Time(00, 00), new Time(23, 30), -180),
				$(new Time(00, 00), new Time(11, 00), -360),
				$(new Time(00, 00), new Time(06, 01), -6 * 360 + 6),
				$(new Time(00, 00), new Time(06, 00), +6 * 360), // could be -6
				// * 360 -
				// it does
				// not
				// matter
				$(new Time(00, 00), new Time(05, 59), +6 * 360 - 6),
				// // start 03:00
				$(new Time(03, 00), new Time(01, 00), -2 * 360),
				$(new Time(03, 00), new Time(06, 00), +3 * 360),
				$(new Time(03, 00), new Time(15, 00), +0),
				$(new Time(03, 00), new Time(14, 00), -360),
				$(new Time(03, 00), new Time(16, 00), +360),
				$(new Time(03, 00), new Time(10, 00), -5 * 360),
				$(new Time(03, 00), new Time(8, 00), 5 * 360),
				$(new Time(9, 00), new Time(20, 00), -360),
				$(new Time(11, 46), new Time(00, 06),
						(int) ((20f / 60f) * 360f))

		);
	}

	@Parameters
	@Test
	public void should_getTime(Time initTime, int angle, Time expectedTime)
			throws Exception {
		// given

		// when
		Time time = TimeAngleUtils.getTime(initTime, angle);

		// then
		assertThat(time).isEqualTo(expectedTime);
	}

	Object[] parametersForShould_getTime() {
		return $($(new Time(00, 00), 360, new Time(01, 00)),
				$(new Time(00, 00), -360, new Time(11, 00)),
				$(new Time(03, 00), 360, new Time(04, 00)),
				$(new Time(03, 00), -360, new Time(02, 00)));
	}
}
