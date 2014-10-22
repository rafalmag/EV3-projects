package pl.rafalmag.ev3.trackedloader

import spock.lang.Specification

class SeekSamplesTest extends Specification {
    // The bearing values range from -12 to +12
    // (with values increasing clockwise when looking from behind the sensor).
    // A bearing of 0 indicates the beacon is directly in front of the sensor.
    // Distance values (0-100) are in cm

    // if no beacon is detected a bearing of 0 and a distance of 255 is returned

    def "should parse seek samples"() {
        given:
        float[] rawData = [-12, 10, 0, 15, 5, 100, 0, 255]
        when:
        def samples = SeekSamples.parseSeekSamples(rawData)
        then:
        samples.getBeacons().size() == 4
        samples.getBeaconsPresent().size() == 3
        samples.getBeacon(0) == new SeekSample(channel: 0, bearing: -12, distanceCm: 10)
        samples.getBeacon(1) == new SeekSample(channel: 1, bearing: 0, distanceCm: 15)
        samples.getBeacon(2) == new SeekSample(channel: 2, bearing: 5, distanceCm: 100)
        samples.getBeacon(3) == new SeekSample(channel: 3, bearing: 0, distanceCm: 255)
        !samples.getBeacon(3).isPresent()
        samples.getClosestBeacon().get() == new SeekSample(channel: 0, bearing: -12, distanceCm: 10)
    }

    def "should parse no samples"() {
        given:
        float[] rawData = [0, 255, 0, 255, 0, 255, 0, 255]
        when:
        def samples = SeekSamples.parseSeekSamples(rawData)
        then:
        samples.getBeacons().size() == 4
        samples.getBeaconsPresent().size() == 0
        !samples.getClosestBeacon().present
        !samples.getClosestBeacon().orNull()
    }
}
