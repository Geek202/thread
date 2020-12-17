package me.geek.tom.thread.api.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.geek.tom.thread.api.network.NetworkedThread
import me.geek.tom.thread.api.util.Colour
import org.slf4j.LoggerFactory

val LOGGER = LoggerFactory.getLogger("thread-networking-example")

/**
 * The same as the Ws281xHueFade, but uses a [NetworkedThread] to a remote server.
 */
fun main(args: Array<String>) {

    if (args.size < 2) {
        LOGGER.error("Usage: command <host> <port>")
        return
    }

    // Read the host and port from commandline arguments.
    val host = args[0]
    val port = args[1].toInt()
    LOGGER.info("Host: $host, port: $port")

    // Create a remote thread.
    val thread = NetworkedThread(host, port)

    // Wait for the NetworkedThread to be ready.
    while (!thread.isReady()) {
        Thread.sleep(5L)
    }

    // Launch a coroutine (some of the LedThread methods are suspend).
    // See https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html for more info on Kotlin coroutines.
    runBlocking {

        for (pixel in 0 until thread.getLength()) {
            thread.setPixel(pixel, Colour.fromHue((pixel * 256 / thread.getLength()) and 255))
        }
        thread.render()
        LOGGER.info("Press enter to continue...")
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
