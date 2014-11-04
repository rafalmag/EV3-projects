package pl.rafalmag.ev3.trackedloader

import com.google.common.math.DoubleMath
import groovy.transform.Immutable

@Immutable
class SeekSample {

    int channel
    /**
     * -12 - 12
     */
    float bearing

    /**
     * 0-100 cm
     */
    float distanceCm

    boolean isPresent() {
        !DoubleMath.fuzzyEquals(distanceCm, 255, 1);
    }
}
