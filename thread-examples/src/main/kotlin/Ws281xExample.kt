package me.geek.tom.thread.api.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.geek.tom.thread.api.util.Colour

fun main() {

    // Create a new LED thread.
    val thread = createLedThread()

    // Launch a coroutine (some of the LedThread methods are suspend).
    // See https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html for more info on Kotlin coroutines.
    runBlocking {
        while (true) {
            thread.fillThread(Colour(255, 0, 0))
            thread.render()
            delay(1000L)
            thread.fillThread(Colour(255, 255, 0))
            thread.render()
            delay(1000L)
            thread.fillThread(Colour(0, 255, 0))
            thread.render()
            delay(1000L)
            thread.fillThread(Colour(0, 255, 255))
            thread.render()
            delay(1000L)
            thread.fillThread(Colour(0, 0, 255))
            thread.render()
            delay(1000L)
            thread.fillThread(Colour(255, 0, 255))
            thread.render()
            delay(1000L)
        }
    }
}
