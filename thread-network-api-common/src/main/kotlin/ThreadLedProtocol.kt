package me.geek.tom.thread.api.network

import me.geek.tom.thread.api.network.packets.c2s.C2SRedrawStripPacket
import me.geek.tom.thread.api.network.packets.c2s.C2SSetPixelPacket
import me.geek.tom.thread.api.network.packets.c2s.C2SSetPixelsPacket
import me.geek.tom.thread.api.network.packets.s2c.S2CStripInfoPacket
import me.geek.tom.thread.netlib.api.listener.ClientEventListener
import me.geek.tom.thread.netlib.api.listener.ServerEventListener
import me.geek.tom.thread.netlib.api.proto.NetProtocol


/**
 * The protocol used for communication with a remote [me.geek.tom.thread.api.LedThread]
 */
class ThreadLedProtocol(
    private val serverHandlerSupplier: () -> ServerEventListener,
    private val clientHandlerSupplier: () -> ClientEventListener
) : NetProtocol() {
    override fun registerServer() {
        this.registerIncoming(0x1, C2SSetPixelPacket::class.java)
        this.registerIncoming(0x2, C2SSetPixelsPacket::class.java)
        this.registerIncoming(0x3, C2SRedrawStripPacket::class.java)
        this.registerOutgoing(S2CStripInfoPacket::class.java, 0x4)
    }

    override fun registerClient() {
        this.registerOutgoing(C2SSetPixelPacket::class.java, 0x1)
        this.registerOutgoing(C2SSetPixelsPacket::class.java, 0x2)
        this.registerOutgoing(C2SRedrawStripPacket::class.java, 0x3)
        this.registerIncoming(0x4, S2CStripInfoPacket::class.java)
    }

    override fun createServerChannelHandler(): ServerEventListener {
        return serverHandlerSupplier.invoke()
    }

    override fun createClientChannelHandler(): ClientEventListener {
        return clientHandlerSupplier.invoke()
    }
}
