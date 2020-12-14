package me.geek.tom.thread.api.network.server.handler

import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.runBlocking
import me.geek.tom.thread.api.LedThread
import me.geek.tom.thread.api.network.packets.c2s.C2SRedrawStripPacket
import me.geek.tom.thread.api.network.packets.c2s.C2SSetPixelPacket
import me.geek.tom.thread.api.network.packets.c2s.C2SSetPixelsPacket
import me.geek.tom.thread.api.network.packets.s2c.S2CStripInfoPacket
import me.geek.tom.thread.netlib.api.listener.ServerEventListener
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.server.Server

class ThreadServerEventHandler(
    private val thread: LedThread
) : ServerEventListener {
    override fun onPacketReceived(server: Server, ctx: ChannelHandlerContext, packet: Packet) {
        runBlocking {
            when (packet) {
                is C2SRedrawStripPacket -> thread.render()
                is C2SSetPixelPacket -> thread.setPixel(packet.pixel, packet.colour)
                is C2SSetPixelsPacket -> packet.pixels.forEach { (p, c) -> thread.setPixel(p, c) }
            }
        }
    }

    override fun onConnection(server: Server, ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(S2CStripInfoPacket(thread.getLength()))
    }
}
