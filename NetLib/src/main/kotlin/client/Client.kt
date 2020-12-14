package me.geek.tom.thread.netlib.api.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import me.geek.tom.thread.netlib.api.netty.ClientInitialiser
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import me.geek.tom.thread.netlib.api.proto.Packet
import org.slf4j.LoggerFactory

/**
 * A client that handles a connection to a remote [me.geek.tom.thread.netlib.api.server.Server].
 *
 * @param host The hostname or IP to connect to.
 * @param port The TCP port to connect to
 * @param protocol An implementation of [NetProtocol] to communicate to the server with.
 */
class Client(
    private val host: String = "127.0.0.1",
    private val port: Int,
    private val protocol: NetProtocol,
) {

    private var channel: Channel? = null
    private var group: NioEventLoopGroup? = null

    /**
     * Connects this client to the server IP and port passed in the constructor.
     * @param wait Should the current thread be blocked while waiting for a connection.
     */
    fun connect(wait: Boolean = false) {
        this.group = NioEventLoopGroup()
        LOGGER.info("Using ${this.group!!.executorCount()} threads for Netty based client IO")

        val bootstrap = Bootstrap()
        bootstrap.group(this.group!!)
            .channel(NioSocketChannel::class.java)
            .handler(ClientInitialiser(this, this.protocol))
            .remoteAddress(this.host, this.port)

        LOGGER.info("Client connecting to $host:$port...")
        val future = bootstrap.connect()
        if (wait) {
            future.sync()
            LOGGER.info("Client connected to $host:$port!")
        } else {
            future.addListener {
                LOGGER.info("Client connected to $host:$port!")
            }
        }
        this.channel = future.channel()
    }

    /**
     * Shuts down the thread pool used by the internal Netty [Channel]. This causes this Client to become disconnected from the server.
     */
    fun disconnect() {
        if (this.group != null) {
            this.group?.shutdownGracefully()
        }
    }

    /**
     * Sends the given [Packet] to the connected server.
     */
    fun sendPacket(packet: Packet) {
        this.channel?.writeAndFlush(packet)
    }

    /**
     * Returns true if this client's [Channel] exists and if it [Channel.isOpen]
     */
    fun isConnected(): Boolean {
        return this.channel != null && this.channel?.isOpen == true
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Client::class.java)
    }
}
