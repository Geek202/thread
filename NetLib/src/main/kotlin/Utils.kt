package me.geek.tom.thread.netlib.api

import io.netty.buffer.ByteBuf
import java.io.IOException

/**
 * Reads a byte array from a Netty [ByteBuf]
 */
fun ByteBuf.readByteArray(len: Int): ByteArray {
    if (len < 0) throw IllegalArgumentException("Length should be positive, not $len")
    val b = ByteArray(len)
    this.readBytes(b)
    return b
}

/**
 * Read a [String] from a Netty [ByteBuf].
 * See [writeString] for an explanation of the encoding
 * @see writeString
 */
fun ByteBuf.readString(): String {
    val len = this.readInt()
    val bytes = this.readByteArray(len)
    return String(bytes, Charsets.UTF_8)
}

/**
 * Write a [String] to a Netty [ByteBuf].
 * They are encoded by writing the length of the string as an [Int], then the UTF-8 encoded bytes immediately after.
 * There is a limit of 32767 on the length of these bytes.
 */
fun ByteBuf.writeString(s: String) {
    val bytes = s.toByteArray(Charsets.UTF_8)
    if (bytes.size > 32767) {
        throw IOException("String too big (was ${bytes.size} bytes encoded, max 32767)")
    } else {
        this.writeInt(bytes.size)
        this.writeBytes(bytes)
    }
}
