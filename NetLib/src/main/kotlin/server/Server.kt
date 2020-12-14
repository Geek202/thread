package me.geek.tom.thread.netlib.api.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import me.geek.tom.thread.netlib.api.netty.ServerInitialiser
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import org.slf4j.LoggerFactory
import java.util.function.Supplier

/**
 * A server that handles connections from [me.geek.tom.thread.netlib.api.client.Client]s.
 *
 * @param host The hostname to accept connections to.
 * @param port The TCP port to listen for connections on.
 * @param protocolSupplier A [Supplier] that creates new [NetProtocol] instances for each incoming connection.
 */
class Server(
    private val host: String = "127.0.0.1",
    private val port: Int,
    private val protocolSupplier: Supplier<NetProtocol>,
) {

    private var channel: Channel? = null
    private var bossGroup: NioEventLoopGroup? = null
    private var workerGroup: NioEventLoopGroup? = null

    /**
     * Binds this server to the host and port passed in the constructor.
     * @param wait Whether or not the current thread should be blocked until the server connects.
     */
    fun bind(wait: Boolean = false) {
        this.bossGroup = NioEventLoopGroup(1)
        this.workerGroup = NioEventLoopGroup()
        LOGGER.info("Using ${this.workerGroup!!.executorCount()} worker thread(s) " +
                "and ${this.bossGroup!!.executorCount()} boss thread(s) for Netty based server IO")

        val b = ServerBootstrap()

        val protocol = this.protocolSupplier.get()

        b.group(this.bossGroup!!, this.workerGroup!!)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(ServerInitialiser(this, protocol))

        LOGGER.info("Starting listening on $host:$port...")
        val f = b.localAddress(this.host, this.port).bind()
        if (wait) f.sync()
        LOGGER.info("Now listening for connections on $host:$port!")
    }

    /**
     * Shuts down this server by closing its Netty [Channel] and [NioEventLoopGroup]s.
     * @param wait Should the current thread be blocked while waiting for things to be shutdown.
     */
    fun close(wait: Boolean = false) {
        LOGGER.info("Shutting down channel...")
        if (this.channel != null) {
            if (this.channel?.isOpen == true) {
                val future = this.channel?.close()
                if (wait) future?.sync()
                this.channel = null
            }
        }

        LOGGER.info("Shutting down boss executor...")
        if (this.bossGroup != null) {
            val future = this.bossGroup?.shutdownGracefully()
            if (wait) future?.sync()
            this.bossGroup = null
        }

        LOGGER.info("Shutting down workers...")
        if (this.workerGroup != null) {
            val future = this.workerGroup?.shutdownGracefully()
            if (wait) future?.sync()
            this.workerGroup = null
        }

        LOGGER.info("Server stopped!")
    }

    /**
     * Returns true if the server [Channel] exists and is open.
     */
    fun isListening(): Boolean {
        return this.channel != null && this.channel?.isOpen == true
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Server::class.java)
    }
}
