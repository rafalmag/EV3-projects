package pl.rafalmag.ev3.trackedloader

import groovy.transform.Canonical
import groovy.transform.Immutable
import groovy.transform.TupleConstructor
import lejos.hardware.sensor.SensorMode

@Immutable
class SeekSensor {

    final SensorMode seekSensor

    SeekSamples getSeekSamples(){
        def seekSamples = new float[seekSensor.sampleSize()]
        seekSensor.fetchSample(seekSamples,0)
        return SeekSamples.parseSeekSamples(seekSamples)
    }
}
