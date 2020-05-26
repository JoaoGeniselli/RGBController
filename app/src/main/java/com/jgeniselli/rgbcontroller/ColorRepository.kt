package com.jgeniselli.rgbcontroller

interface ColorRepository {
    suspend fun applyColor(color: Color)
    suspend fun connectToDevice(pairedDeviceAddress: String)
    suspend fun disconnect()
}

class Color private constructor(
    private val red: Int, private val green: Int, private val blue: Int
) {
    fun toLightDevicePattern(): String {
        val format = { value: Int -> String.format("%03d", value) }
        return "${format(red)}|${format(green)}|${format(blue)}"
    }

    companion object {

        fun create(red: Int, green: Int, blue: Int): Color {
            validateColorValues(red, green, blue)
            return Color(red, green, blue)
        }

        private fun validateColorValues(vararg values: Int) {
            val validColorRange = IntRange(0, 255)
            values.forEach {
                if (!validColorRange.contains(it))
                    throw IllegalArgumentException("Color values must be in range 0 ~ 255")
            }
        }
    }
}