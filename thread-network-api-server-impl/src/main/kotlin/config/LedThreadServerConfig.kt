package me.geek.tom.thread.api.network.server.config

import com.github.mbelling.ws281x.LedStripType
import com.github.mbelling.ws281x.Ws281xLedStrip
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.toml
import me.geek.tom.thread.api.LedThread
import me.geek.tom.thread.api.LedThreadConfig
import me.geek.tom.thread.api.network.server.parseBoolean
import me.geek.tom.thread.api.network.server.parseInt
import me.geek.tom.thread.api.piWs281x.toThread
import org.slf4j.LoggerFactory

/**
 * Holder for the settings in [DriverSpec] and [ServerSpec]
 */
class LedThreadServerConfig(
    private val config: Config
) {

    // Server config
    val serverHost: String get() = config[ServerSpec.host]
    val serverPort: Int get() = config[ServerSpec.port]

    // Driver config
    val driver: LedThread by lazy {
        when (val driverType = config[DriverSpec.driverType]) {
            "rpi-ws281x" -> createRpiWs281xDriver(config[DriverSpec.driverConfig])
            else -> createCustomDriver(driverType, config[DriverSpec.driverConfig])
        }
    }

    private fun createCustomDriver(driverType: String, config: Map<String, String>): LedThread {
        try {
            val cls = Class.forName(driverType)
            if (!LedThread::class.java.isAssignableFrom(cls)) {
                LOGGER.error("Custom driver class: $driverType should extend ${LedThread::class.java.name}!")
                throw IllegalArgumentException("Invalid custom driver class!")
            }
            val constructor = cls.getDeclaredConstructor(LedThreadConfig::class.java)
            return constructor.newInstance(LedThreadConfig(config)) as LedThread
        } catch (e: ClassNotFoundException) {
            LOGGER.error("Failed to locate custom driver class: $driverType!", e)
            throw IllegalArgumentException("Invalid custom driver class!")
        } catch (e: NoSuchMethodError) {
            LOGGER.error("Custom driver class: $driverType should have a constructor that accepts an instance of ${LedThreadConfig::class.java.name}", e)
            throw IllegalArgumentException("Invalid custom driver class!")
        }
    }

    private fun createRpiWs281xDriver(config: Map<String, String>): LedThread {
        LOGGER.info("Config: $config")
        // ew.
        return Ws281xLedStrip(
            (config["ledCount"]?: error("Need to set led_count!")).parseInt(),
            config["ledPin"]?.parseInt()?: 18,
            config["frequency"]?.parseInt()?: 800000,
            config["dma"]?.parseInt()?: 10,
            config["brightness"]?.parseInt()?: 255,
            config["pwmChannel"]?.parseInt()?: 0,
            config["invert"]?.parseBoolean()?: false,
            LedStripType.valueOf(config["stripType"]?.toUpperCase()?: "WS2811_STRIP_RGB"),
            config["clearOnExit"]?.parseBoolean()?: false
        ).toThread()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LedThreadServerConfig::class.java)
    }
}

// Loads the config from a file named 'server_config.toml' in the current working directory.
val serverConfig = LedThreadServerConfig(
    Config {
        addSpec(DriverSpec)
        addSpec(ServerSpec)
    }.from.toml.file("server_config.toml")
        .from.systemProperties()
)
