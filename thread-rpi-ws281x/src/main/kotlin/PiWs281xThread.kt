package me.geek.tom.thread.api.piWs281x

import com.github.mbelling.ws281x.Color
import com.github.mbelling.ws281x.Ws281xLedStrip
import me.geek.tom.thread.api.LedThread
import me.geek.tom.thread.api.util.Colour


/**
 * Implementation of [LedThread] that uses a hardware [Ws281xLedStrip] as the output.
 */
class PiWs281xThread(
    private val internalStrip: Ws281xLedStrip
) : LedThread {
    override fun setPixel(pixel: Int, colour: Colour) {
        internalStrip.setPixel(pixel, colour.toWs281xColor())
    }

    override fun setPixel(pixel: Int, r: Int, g: Int, b: Int) {
        internalStrip.setPixel(pixel, r, g, b)
    }

    override fun fillThread(col: Colour) {
        internalStrip.setStrip(col.toWs281xColor())
    }

    override fun fillThread(r: Int, g: Int, b: Int) {
        internalStrip.setStrip(r, g, b)
    }

    override suspend fun render() {
        internalStrip.render()
    }

    override fun getLength(): Int {
        return internalStrip.ledsCount
    }

    override suspend fun release() {

    }
}

/**
 * Helper to convert a [Colour] into a [Color] from the rpi-ws281x library.
 */
fun Colour.toWs281xColor(): Color {
    return Color(red, green, blue)
}

/**
 * Helper to wrap a [Ws281xLedStrip] in a [PiWs281xThread].
 */
fun Ws281xLedStrip.toThread(): LedThread {
    return PiWs281xThread(this)
}
