package me.geek.tom.thread.api.network

import io.netty.channel.ChannelHandlerContext
import me.geek.tom.thread.api.LedThread
import me.geek.tom.thread.api.network.packets.c2s.C2SRedrawStripPacket
import me.geek.tom.thread.api.network.packets.c2s.C2SSetPixelPacket
import me.geek.tom.thread.api.network.packets.s2c.S2CStripInfoPacket
import me.geek.tom.thread.api.util.Colour
import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.listener.ClientEventListener
import me.geek.tom.thread.netlib.api.listener.NoopListener
import me.geek.tom.thread.netlib.api.proto.Packet
import org.slf4j.LoggerFactory


/**
 * An [LedThread] implementation that connects over the network using the [ThreadLedProtocol]
 *
 * [setPixel] and [render] are not safe unless [isReady] is true.
 */
class NetworkedThread(
    private val host: String,
    private val port: Int
) : LedThread, ClientEventListener {

    private val client: Client = Client(this.host, this.port, ThreadLedProtocol(::NoopListener) { this })
    private var ready: Boolean = false

    private var length: Int = -1

    init {
        this.client.connect(wait = true)
    }

    override fun setPixel(pixel: Int, colour: Colour) {
        if (!ready) throw IllegalStateException("Strip is not connected!")
        this.client.sendPacket(C2SSetPixelPacket(pixel, colour))
    }

    override suspend fun render() {
        if (!ready) throw IllegalStateException("Strip is not connected!")
        this.client.sendPacket(C2SRedrawStripPacket((0 until getLength()).toCollection(ArrayList())))
    }

    override fun getLength(): Int {
        if (!ready) throw IllegalStateException("Strip is not connected!")
        return length
    }

    override suspend fun release() {
        if (!ready) throw IllegalStateException("Strip is not connected!")
        this.client.disconnect()
    }

    override fun isReady(): Boolean {
        return this.ready
    }

    override fun onPacketReceived(client: Client, ctx: ChannelHandlerContext, packet: Packet) {
        LOGGER.info("Packet: $packet")
        when (packet) {
            is S2CStripInfoPacket -> {
                this.ready = true
                this.length = packet.ledCount
            }
        }
    }

    override fun onConnected(client: Client) { }

    override fun onDisconnected(client: Client) {
        this.ready = false
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NetworkedThread::class.java)
    }
}
