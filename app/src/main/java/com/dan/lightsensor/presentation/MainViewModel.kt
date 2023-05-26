package com.dan.lightsensor.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dan.lightsensor.domain.sensor.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val lightSensor: MeasurableSensor
) : ViewModel() {

    val isDark = mutableStateOf(false)

    init {
        lightSensor.setOnNewInputListener { sensorInputs ->
            val lux = sensorInputs[0]
            isDark.value = lux < 60f
        }
        lightSensor.start()
    }
}