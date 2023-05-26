package com.dan.lightsensor.dependency

import android.app.Application
import com.dan.lightsensor.domain.sensor.LightSensor
import com.dan.lightsensor.domain.sensor.MeasurableSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorsModule {

    @Provides
    @Singleton
    fun providesLightSensor(app: Application): MeasurableSensor =
        LightSensor(app)
}