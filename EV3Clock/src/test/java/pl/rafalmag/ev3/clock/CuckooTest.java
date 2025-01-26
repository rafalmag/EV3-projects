package pl.rafalmag.ev3.clock;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import pl.rafalmag.ev3.Time;

@RunWith(JUnitParamsRunner.class)
public class CuckooTest {

	@Parameters
	@Test
	public void should_cuckoo(Time analogTime, Time tickTime) throws Exception {
		// given

		// when
		boolean shouldCuckoo = Cuckoo.shouldCuckoo(analogTime, tickTime);

		// then
		assertThat(shouldCuckoo, equalTo(true));
	}

	Object[] parametersForShould_cuckoo() {
		return $($(new Time(12, 00), new Time(00, 20)),
				$(new Time(12, 9), new Time(00, 20)),
				$(new Time(11, 51), new Time(00, 20)),
				$(new Time(11, 50), new Time(00, 20)),
				$(new Time(12, 00), new Time(00, 01)));
	}

	@Parameters
	@Test
	public void should_not_cuckoo(Time analogTime, Time tickTime)
			throws Exception {
		// given

		// when
		boolean shouldCuckoo = Cuckoo.shouldCuckoo(analogTime, tickTime);

		// then
		assertThat(shouldCuckoo, equalTo(false));
	}

	Object[] parametersForShould_not_cuckoo() {
		return $($(new Time(12, 11), new Time(00, 20)),
				$(new Time(11, 39), new Time(00, 20)),
				$(new Time(12, 10), new Time(00, 20)),
				$(new Time(12, 01), new Time(00, 01)),
				$(new Time(11, 59), new Time(00, 01)));
	}

}
