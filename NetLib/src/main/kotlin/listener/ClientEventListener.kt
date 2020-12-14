package me.geek.tom.thread.netlib.api.listener

import io.netty.channel.ChannelHandlerContext
import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.proto.Packet

/**
 * A listener to packets and other events from a [Client].
 */
interface ClientEventListener {
    /**
     * Called when a packet is received from the server.
     */
    fun onPacketReceived(client: Client, ctx: ChannelHandlerContext, packet: Packet)

    /**
     * Called after the connection to the server has been created
     */
    fun onConnected(client: Client)

    /**
     * Called when the [Client]'s connection to the server is broken, or the [Client.disconnect] method is called.
     */
    fun onDisconnected(client: Client)
}
