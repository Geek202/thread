package me.geek.tom.thread.api.network.packets.s2c

import io.netty.buffer.ByteBuf
import me.geek.tom.thread.netlib.api.proto.Packet

/**
 * Contains information about the strip on the server. Currently just the length.
 */
class S2CStripInfoPacket(
    var ledCount: Int
) : Packet() {

    constructor(): this(-1)

    override fun read(buf: ByteBuf) {
        this.ledCount = buf.readInt()
    }

    override fun write(buf: ByteBuf) {
        buf.writeInt(this.ledCount)
    }
}