package me.geek.tom.thread.api.network.packets.c2s

import io.netty.buffer.ByteBuf
import me.geek.tom.thread.api.util.Colour
import me.geek.tom.thread.netlib.api.proto.Packet

/**
 * Sets a multiple pixels on the remote thread.
 * Should not redraw the pixels until a [me.geek.tom.thread.api.network.packets.c2s.C2SRedrawStripPacket] is sent.
 */
class C2SSetPixelsPacket(
    var pixels: Map<Int, Colour>
) : Packet() {

    constructor(): this(mapOf())

    override fun read(buf: ByteBuf) {
        val count = buf.readInt()
        val pixels = HashMap<Int, Colour>()
        for (i in 1..count) {
            pixels[buf.readInt()] = Colour(buf.readInt())
        }
        this.pixels = pixels
    }

    override fun write(buf: ByteBuf) {
        buf.writeInt(this.pixels.size)
        for ((pixel, col) in this.pixels) {
            buf.writeInt(pixel)
            buf.writeInt(col.toInt())
        }
    }
}