package me.geek.tom.thread.api.util

import kotlin.math.roundToInt

/**
 * And object that contains red, green and blue fields to represent a colour.
 */
data class Colour(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    /**
     * Unpacks an [Int] in the form <code>0xRRGGBB</code>.
     */
    constructor(col: Int) : this(
        col shr 16 and 0xFF,
        col shr 8 and 0xFF,
        col and 0xFF
    )

    /**
     * Packs this [Colour] into a single [Int] in the form <code>0xRRGGBB</code>
     */
    fun toInt(): Int {
        return ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF)
    }

    operator fun times(i: Double): Colour {
        return Colour((this.red * i).roundToInt(), (this.green * i).roundToInt(), (this.blue * i).roundToInt())
    }

    companion object {
        /**
         * Converts a hue from into a colour. The range is 0-255 inclusive but is wrapped if it exceeds this range.
         */
        fun fromHue(hue: Int): Colour {
            val wrappedHue = hue % 256
            return when {
                wrappedHue < 85 -> Colour(hue * 3, 255 - hue * 3, 0)
                wrappedHue < 170 -> Colour(255 - (wrappedHue - 85) * 3, 0, (wrappedHue - 85) * 3)
                else -> Colour(0, (wrappedHue - 170) * 3, 255 - (wrappedHue - 170) * 3)
            }
        }
    }
}
