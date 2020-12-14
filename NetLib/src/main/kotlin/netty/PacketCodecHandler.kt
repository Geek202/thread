package me.geek.tom.thread.netlib.api.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import me.geek.tom.thread.netlib.api.proto.Packet

class PacketCodecHandler(
    private val protocol: NetProtocol
) : ByteToMessageCodec<Packet>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        val initialIndex = out.writerIndex()

        try {
            val packetId = this.protocol.getOutgoingId(msg)
            out.writeInt(packetId)
            msg.write(out)
        } catch (t: Throwable) {
            out.writerIndex(initialIndex)
            throw t
        }
    }

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        val initialIndex = buf.readerIndex()
        try {
            val id = buf.readInt()
            if (id == -1) {
                buf.readerIndex(initialIndex)
                return
            }
            val packet = protocol.createIncoming(id)
            packet.read(buf)

            if (buf.readableBytes() > 0) {
                throw IllegalStateException("Packet $id (${packet::class.java.name}) failed to fully read!")
            }

            out += packet
        } catch (t: Throwable) {
            buf.readerIndex(buf.readerIndex() + buf.readableBytes())
            throw t
        }
    }
}
