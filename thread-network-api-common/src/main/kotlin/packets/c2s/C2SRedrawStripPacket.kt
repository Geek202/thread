package me.geek.tom.thread.api.network.packets.c2s

import io.netty.buffer.ByteBuf
import me.geek.tom.thread.netlib.api.proto.Packet

/**
 * Instructs the strip that it should be redrawn.
 * @see [me.geek.tom.thread.api.LedThread.render]
 */
class C2SRedrawStripPacket(
    var pixels: List<Int>?
) : Packet() {

    constructor(): this(null)

    override fun read(buf: ByteBuf) {
        val len = buf.readInt()
        val pixels = ArrayList<Int>()
        for (i in 1..len) {
            pixels += buf.readInt()
        }
        this.pixels = pixels
    }

    override fun write(buf: ByteBuf) {
        val pixels = this.pixels!!
        buf.writeInt(pixels.size)
        for (pixel in pixels) {
            buf.writeInt(pixel)
        }
    }
}
