package me.geek.tom.thread.api.network.server

import me.geek.tom.thread.api.network.ThreadLedProtocol
import me.geek.tom.thread.api.network.server.config.serverConfig
import me.geek.tom.thread.api.network.server.handler.ThreadServerEventHandler
import me.geek.tom.thread.netlib.api.listener.NoopListener
import me.geek.tom.thread.netlib.api.server.Server

// Utils
fun String.parseInt(): Int {
    return Integer.parseInt(this)
}

fun String.parseBoolean(): Boolean {
    return java.lang.Boolean.parseBoolean(this)
}

// Kick it all into motion
fun main() {
    val server = Server(host = serverConfig.serverHost, port = serverConfig.serverPort) {
        ThreadLedProtocol({ ThreadServerEventHandler(serverConfig.driver) }, ::NoopListener)
    }
    server.bind(true)
    while (server.isListening()) {
        Thread.sleep(5L)
    }
}
