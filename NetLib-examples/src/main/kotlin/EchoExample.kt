package me.geek.tom.thread.netlib.example

import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.server.Server
import me.geek.tom.thread.netlib.example.packets.echo.EchoPacket
import me.geek.tom.thread.netlib.example.protocols.EchoProtocol

/**
 * A simple example of how to use NetLib. Starts up a [Server] and [Client] using a simple [EchoProtocol] and sends a message.
 */
fun main() {
    // Create a new server instance that listens on localhost:2323 using the EchoProtocol
    val server = Server(port = 2323, protocolSupplier = ::EchoProtocol)
    // Makes the server start listening to connections and waits until it is ready.
    server.bind(wait = true)

    // Creates a new client that will connect to localhost:2323 using the EchoProtocol.
    val client = Client(port = 2323, protocol = EchoProtocol())
    // Connects the client to the server and blocks until a connection is established.
    client.connect(wait = true)
    // Sends the message 'Hello, World!' to the server.
    client.sendPacket(EchoPacket("Hello, World!"))

    // Wait until the server and client disconnect.
    while (client.isConnected() && server.isListening()) {
        Thread.sleep(5L)
    }
}
