package pl.rafalmag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CordsToAngleTest {

	@Parameters({ "0,0,0", "1,0,0", "0,1,270", "0,-1,90", "-1,0,180",
			"0,1,270", "1,-1,45", "-1,-1,135", "-1,1,225", "1,1,315" })
	@Test
	public void should_return_angle_for_coordinates(double x, double y,
			double expectedAngle) {
		// given
		// when
		double angle = CordsToAngle.getAngleForCoordinates(x, y);

		// then
		assertThat(angle).isEqualTo(expectedAngle, offset(0.1));
	}
}
