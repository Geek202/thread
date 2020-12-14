package me.geek.tom.thread.netlib.example.protocols

import io.netty.channel.ChannelHandlerContext
import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.listener.ClientEventListener
import me.geek.tom.thread.netlib.api.listener.ServerEventListener
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.server.Server
import me.geek.tom.thread.netlib.example.packets.echo.EchoPacket
import org.slf4j.LoggerFactory
/**
 * A simple [NetProtocol] implementation of the ECHO protocol.
 */
class EchoProtocol : NetProtocol() {

    // Register a new packet type that can be sent and received with ID 0x0. This is done on both the server and the client.
    override fun registerServer() {
        this.register(0x0, EchoPacket::class.java)
    }

    override fun registerClient() {
        this.register(0x0, EchoPacket::class.java)
    }

    // Create a listener to events on the server.
    override fun createServerChannelHandler(): ServerEventListener {
        return EchoServerHandler()
    }

    // Create a listener for events on the client.
    override fun createClientChannelHandler(): ClientEventListener {
        return EchoClientHandler()
    }

    /**
     * An implementation of [ServerEventListener] for the ECHO protocol.
     */
    class EchoServerHandler : ServerEventListener {
        override fun onPacketReceived(server: Server, ctx: ChannelHandlerContext, packet: Packet) {
            if (packet is EchoPacket) { // Check if this is an EchoPacket.

                // Display a message.
                LOGGER.info("ECHO MESSAGE FROM ${ctx.channel().remoteAddress()}: ${packet.message}")

                // Send the same message back to the client like an ECHO.
                ctx.channel().writeAndFlush(EchoPacket(packet.message))
                        // Listen to when the packet has been send
                    .addListener {
                        LOGGER.info("Echo server done, stopping")
                        // Shutdown the server and do not block waiting for it to stop
                        server.close(wait = false)
                    }
            }
        }

        override fun onConnection(server: Server, ctx: ChannelHandlerContext) { }

        companion object {
            private val LOGGER = LoggerFactory.getLogger(EchoServerHandler::class.java)
        }
    }

    class EchoClientHandler : ClientEventListener {
        override fun onPacketReceived(client: Client, ctx: ChannelHandlerContext, packet: Packet) {
            if (packet is EchoPacket) { // Check if we have received an EchoPacket.
                LOGGER.info("ECHO MESSAGE: ${packet.message}") // Log a message
                client.disconnect() // Shutdown the client now.
            }
        }

        override fun onConnected(client: Client) { }
        override fun onDisconnected(client: Client) { }

        companion object {
            private val LOGGER = LoggerFactory.getLogger(EchoClientHandler::class.java)
        }
    }
}