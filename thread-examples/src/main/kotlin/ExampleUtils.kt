package me.geek.tom.thread.api.example

import com.github.mbelling.ws281x.LedStripType
import com.github.mbelling.ws281x.Ws281xLedStrip
import me.geek.tom.thread.api.LedThread
import me.geek.tom.thread.api.piWs281x.toThread

/**
 * Creates a new [Ws281xLedStrip] with some default settings and converts it into an [LedThread].
 */
fun createLedThread(ledCount: Int = 16): LedThread {
    return Ws281xLedStrip(
        ledCount,
        18,
        800000,
        10,
        128,
        0,
        false,
        LedStripType.WS2811_STRIP_RGB,
        true
    ).toThread()
}
