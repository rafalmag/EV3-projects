package pl.rafalmag.ev3.trackedloader

import lejos.hardware.sensor.SensorMode
import lejos.robotics.SampleProvider
import lejos.robotics.filter.MeanFilter

class SeekSensor {

    final SampleProvider sensor

    SeekSensor(SensorMode seekSensor) {
        this.sensor = new MeanFilter(seekSensor, 3)
    }

    SeekSamples getSeekSamples() {
        def seekSamples = new float[sensor.sampleSize()]
        sensor.fetchSample(seekSamples, 0)

        return SeekSamples.parseSeekSamples(seekSamples)
    }
}
