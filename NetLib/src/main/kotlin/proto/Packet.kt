package me.geek.tom.thread.netlib.api.proto

import io.netty.buffer.ByteBuf

/**
 * Base class that represents data that can be sent over the network.
 */
abstract class Packet {
    /**
     * Read data from the [ByteBuf] into internal fields for use later.
     */
    abstract fun read(buf: ByteBuf)

    /**
     * Write data to the [ByteBuf] ready to be sent over the network.
     */
    abstract fun write(buf: ByteBuf)
}
