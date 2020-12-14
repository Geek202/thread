package me.geek.tom.thread.api.network.packets.c2s

import io.netty.buffer.ByteBuf
import me.geek.tom.thread.api.util.Colour
import me.geek.tom.thread.netlib.api.proto.Packet

/**
 * Sets a single pixel on the remote thread.
 * Should not redraw the pixels until a [me.geek.tom.thread.api.network.packets.c2s.C2SRedrawStripPacket] is sent.
 */
class C2SSetPixelPacket(
    var pixel: Int,
    var colour: Colour
) : Packet() {

    constructor(): this(-1, Colour(0, 0, 0))

    override fun read(buf: ByteBuf) {
        this.pixel = buf.readInt()
        this.colour = Colour(buf.readInt())
    }

    override fun write(buf: ByteBuf) {
        buf.writeInt(this.pixel)
        buf.writeInt(this.colour.toInt())
    }
}
