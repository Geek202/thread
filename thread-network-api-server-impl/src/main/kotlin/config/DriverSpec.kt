package me.geek.tom.thread.api.network.server.config

import com.uchuhimo.konf.ConfigSpec

/**
 * Contains settings related to the [me.geek.tom.thread.api.LedThread] implementation used on the server.
 */
object DriverSpec : ConfigSpec() {

    /**
     * The driver type can be:
     * - one of the default:
     *      - rpi-ws281x
     * - or the name of a custom class on the classpath (eg me.geek.tom.example.SuperCoolLedThread)
     *
     *      - Custom LedThread classes must have a constructor that accepts a <code>Map<String, String></code> that is used to receive the configuration.
     */
    val driverType by required<String>()

    /**
     * Takes any configuration options required by the selected driver.
     */
    val driverConfig by required<Map<String, String>>()

}
