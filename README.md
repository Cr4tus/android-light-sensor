# Android Light Sensor

## What does the app do?
The app uses the light sensor present in the phone to change the background color.

## Programming Languages:
- Kotlin

## Frameworks & Libraries:
- Gradle - https://gradle.org
- Dagger Hilt - https://developer.android.com/training/dependency-injection/hilt-android
- Android Jetpack Suite (including Android Jetpack Compose for UI) - https://developer.android.com/jetpack

## Implementation:
Sensor implementation:
1. Abstraction (declare basic functionalities)
```kotlin
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
```
2. Android Compatibility (define the logic to work for any Android sensor)
``` kotlin
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
```
3. Light Sensor:
``` kotlin
class LightSensor(
    context: Context
) : AndroidSensor(
    context = context,
    feature = PackageManager.FEATURE_SENSOR_LIGHT,
    type = Sensor.TYPE_LIGHT
)
```
The light senso makes possible the pass only the context, the other 2 parameters being always the same, anywhere you want to instantiate such an object. Because we don't want to be able to create more than one instance of the light sensor, we are injecting it using Dagger Hilt as a *Singleton Component*:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SensorsModule {

    @Provides
    @Singleton
    fun providesLightSensor(app: Application): MeasurableSensor =
        LightSensor(app)
}
```
In this case, we need it to be injected in only within the *MainViewModel*:
```kotlin
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
```
