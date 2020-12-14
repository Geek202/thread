package me.geek.tom.thread.netlib.api.netty

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import io.netty.handler.codec.CorruptedFrameException


class PacketSlicingHandler : ByteToMessageCodec<ByteBuf>() {
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val len = msg.readableBytes()
        out.ensureWritable(4 + len) // 4 is the length of an Int
        out.writeInt(len)
        out.writeBytes(msg)
    }

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        val size = 4
        buf.markReaderIndex()
        val lengthBytes = ByteArray(size)
        for (index in lengthBytes.indices) {
            if (!buf.isReadable) {
                buf.resetReaderIndex()
                return
            }

            lengthBytes[index] = buf.readByte()
            if (index == size - 1) {
                val length: Int = Unpooled.wrappedBuffer(lengthBytes).readInt()
                if (buf.readableBytes() < length) {
                    buf.resetReaderIndex()
                    return
                }
                out.add(buf.readBytes(length))
                return
            }
        }
        throw CorruptedFrameException("Length is too long.")
    }
}
