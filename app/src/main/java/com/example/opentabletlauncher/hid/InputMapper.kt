package com.example.opentabletlauncher.hid

import android.view.MotionEvent
import com.example.opentabletlauncher.config.TabletConfig
import kotlin.math.roundToInt

class InputMapper(private val config: TabletConfig) {
    fun map(event: MotionEvent): ByteArray {
        val xNorm = (event.x / config.screenWidth).coerceIn(0f, 1f)
        val yNorm = (event.y / config.screenHeight).coerceIn(0f, 1f)
        val xMapped = if (config.invertX) 1f - xNorm else xNorm
        val yMapped = if (config.invertY) 1f - yNorm else yNorm
        val x = (xMapped * 32767f).roundToInt()
        val y = (yMapped * 32767f).roundToInt()
        val tip = if (event.actionMasked == MotionEvent.ACTION_UP) 0 else 1
        val pressure = if (config.pressureSimulation) (event.pressure.coerceIn(0f, 1f) * 255f).roundToInt() else 180
        return byteArrayOf(
            tip.toByte(),
            (x and 0xFF).toByte(), ((x shr 8) and 0xFF).toByte(),
            (y and 0xFF).toByte(), ((y shr 8) and 0xFF).toByte(),
            (pressure and 0xFF).toByte(),
            0x00,
            0x00
        )
    }
}
