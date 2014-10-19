package pl.rafalmag.ev3.trackedloader

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.ToString

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
        distanceCm != 255 // todo float check!
    }
}
