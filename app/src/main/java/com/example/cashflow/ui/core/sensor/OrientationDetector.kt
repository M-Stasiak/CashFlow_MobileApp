package com.example.cashflow.ui.core.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class OrientationDetector(
    context: Context,
    private val onUpsideDown: () -> Unit,
    private val onUpright: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var isUpsideDown = false
    private var isRunning = false

    fun start() {
        if (isRunning) return
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            isRunning = true
        }
    }

    fun stop() {
        if (!isRunning) return
        sensorManager.unregisterListener(this)
        isRunning = false
    }

    override fun onSensorChanged(event: SensorEvent) {
        val y = event.values[1] // Oś Y = góra/dół

        if (!isUpsideDown && y < -7f) {
            isUpsideDown = true
            onUpsideDown()
        } else if (isUpsideDown && y > 7f) {
            isUpsideDown = false
            onUpright()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

