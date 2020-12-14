package me.geek.tom.thread.netlib.api.listener

import io.netty.channel.ChannelHandlerContext
import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.server.Server

/**
 * A [ClientEventListener] and [ServerEventListener] that does nothing when events are received.
 */
class NoopListener : ClientEventListener, ServerEventListener {
    override fun onPacketReceived(client: Client, ctx: ChannelHandlerContext, packet: Packet) { }
    override fun onConnected(client: Client) { }
    override fun onDisconnected(client: Client) { }

    override fun onPacketReceived(server: Server, ctx: ChannelHandlerContext, packet: Packet) { }
    override fun onConnection(server: Server, ctx: ChannelHandlerContext) { }
}
