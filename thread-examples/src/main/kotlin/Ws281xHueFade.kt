package me.geek.tom.thread.api.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.geek.tom.thread.api.util.Colour

fun main() {
    // Create a new LedThread using the helper function.
    val thread = createLedThread(ledCount = 16)

    // Launch a coroutine (some of the LedThread methods are suspend).
    // See https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html for more info on Kotlin coroutines.
    runBlocking {

        for (pixel in 0 until thread.getLength()) {
            thread.setPixel(pixel, Colour.fromHue((pixel * 256 / thread.getLength()) and 255))
        }

        thread.render()
        println("Press enter to continue...")
        readLine()

        while (true) {
            for (offset in 0..thread.getLength()) {
                for (pixel in 0 until thread.getLength()) {
                    thread.setPixel(pixel, Colour.fromHue(((pixel + offset) * 256 / thread.getLength()) and 255))
                }

                thread.render()
                delay(5L)
            }
        }
    }
}
