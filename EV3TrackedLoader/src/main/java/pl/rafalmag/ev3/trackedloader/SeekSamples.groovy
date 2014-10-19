package pl.rafalmag.ev3.trackedloader

import com.google.common.base.Optional
import groovy.transform.Immutable

@Immutable
class SeekSamples {

    /**
     * Implementation based on {@link lejos.hardware.sensor.EV3IRSensor#getSeekMode()}
     *
     * @param rawData
     * @return
     */
    static SeekSamples parseSeekSamples(float[] rawData) {
        new SeekSamples(beacons: [
                new SeekSample(channel: 0, bearing: rawData[0], distanceCm: rawData[1]),
                new SeekSample(channel: 1, bearing: rawData[2], distanceCm: rawData[3]),
                new SeekSample(channel: 2, bearing: rawData[4], distanceCm: rawData[5]),
                new SeekSample(channel: 3, bearing: rawData[6], distanceCm: rawData[7])
        ])
    }

    List<SeekSample> beacons

    List<SeekSample> getBeaconsPresent() {
        beacons.grep { it.isPresent() }
    }

    SeekSample getBeacon(int channel) {
        beacons[channel]
    }

    Optional<SeekSample> getClosestBeacon() {
        // TODO add { Math.<Float>abs(it.bearing) } ?
        def sorted = getBeaconsPresent().sort { it.distanceCm }
        if (sorted.isEmpty())
            Optional.absent()
        else
            Optional.of(sorted.first())
    }
}
