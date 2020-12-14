package me.geek.tom.thread.netlib.example.packets.echo

import io.netty.buffer.ByteBuf
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.readString
import me.geek.tom.thread.netlib.api.writeString

/**
 * A simple packet that contains a single [String] to be sent and received.
 */
class EchoPacket(
    var message: String
) : Packet() {

    // All packets must have a 0-argument constructor for when they are received.
    constructor(): this("")

    override fun read(buf: ByteBuf) {
        this.message = buf.readString() // Just read a single string into the message field.
    }

    override fun write(buf: ByteBuf) {
        buf.writeString(this.message) // Write the message to the buffer to be sent.
    }

    override fun toString(): String {
        return "EchoPacket(message=$message)"
    }
}
