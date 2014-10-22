package pl.rafalmag.ev3.trackedloader

import groovy.transform.Immutable
import lejos.hardware.sensor.SensorMode

class DistanceSensor {

    final SensorMode distanceSensor

    DistanceSensor(SensorMode distanceSensor) {
        this.distanceSensor = distanceSensor
    }

    float getDistanceM(){
        def distanceSample = new float[distanceSensor.sampleSize()]
        distanceSensor.fetchSample(distanceSample,0)
        distanceSample[0]
    }

    float getDistanceCm(){
        getDistanceM() / 100
    }
}
