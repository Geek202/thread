package me.geek.tom.thread.netlib.api.listener

import io.netty.channel.ChannelHandlerContext
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.server.Server

/**
 * A listener to packets and other events from a [Server].
 */
interface ServerEventListener {
    /**
     * Called when a packet is received from a client.
     */
    fun onPacketReceived(server: Server, ctx: ChannelHandlerContext, packet: Packet)

    /**
     * Called when a new client connects to the server.
     */
    fun onConnection(server: Server, ctx: ChannelHandlerContext)
}
