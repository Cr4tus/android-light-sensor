package com.dan.lightsensor.domain.sensor

import android.hardware.SensorEventListener

abstract class MeasurableSensor : SensorEventListener {

    abstract val type: Int
    abstract val exists: Boolean
    protected var listener: ((List<Float>) -> Unit)? = null

    abstract fun start()

    abstract fun stop()

    fun setOnNewInputListener(listener: (List<Float>) -> Unit) {
        this.listener = listener
    }
}