package com.dan.lightsensor.domain.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context,
    private val feature: String,
    override val type: Int
) : MeasurableSensor() {

    override val exists: Boolean
        get() = context.packageManager.hasSystemFeature(feature)

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    override fun start() {
        if (!exists) {
            return
        }

        if (null == sensorManager) {
            initializeSensorManager()
        }

        if (null == sensor) {
            tryInitializeSensor()
        }

        sensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun initializeSensorManager() {
        sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
    }

    private fun tryInitializeSensor() {
        sensor = sensorManager?.getDefaultSensor(type)
    }

    override fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!exists) {
            return
        }

        if (type == event?.sensor?.type) {
            listener?.invoke(event.values.toList())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}