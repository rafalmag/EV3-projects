package pl.rafalmag.ev3.trackedloader

import groovy.transform.Immutable
import lejos.hardware.sensor.SensorMode

@Immutable
class DistanceSensor {

    final SensorMode distanceSensor

    float getDistanceM(){
        def distanceSample = new float[distanceSensor.sampleSize()]
        distanceSensor.fetchSample(distanceSample,0)
        distanceSample[0]
    }

    float getDistanceCm(){
        getDistanceM() / 100
    }
}
