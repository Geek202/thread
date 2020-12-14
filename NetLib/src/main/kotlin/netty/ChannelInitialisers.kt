package me.geek.tom.thread.netlib.api.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.SocketChannel
import me.geek.tom.thread.netlib.api.client.Client
import me.geek.tom.thread.netlib.api.listener.ClientEventListener
import me.geek.tom.thread.netlib.api.listener.ServerEventListener
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import me.geek.tom.thread.netlib.api.proto.Packet
import me.geek.tom.thread.netlib.api.server.Server

class ServerInitialiser(
    private val server: Server,
    private val protocol: NetProtocol
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        ch.config().setOption(ChannelOption.TCP_NODELAY, false)
        ch.config().setOption(ChannelOption.SO_REUSEADDR, true)
        ch.config().setOption(ChannelOption.IP_TOS, 0x18)

        val p = ch.pipeline()
        p.addLast("slicer", PacketSlicingHandler())
        p.addLast("decoder", PacketCodecHandler(protocol))
        p.addLast("handler", ServerListener(server, protocol.createServerChannelHandler()))

        protocol.registerServer()
    }

    class ServerListener(
        private val server: Server,
        private val listener: ServerEventListener
    ) : SimpleChannelInboundHandler<Packet>() {
        override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
            listener.onPacketReceived(server, ctx, msg)
        }

        override fun channelActive(ctx: ChannelHandlerContext) {
            super.channelActive(ctx)
            this.listener.onConnection(server, ctx)
        }
    }
}

class ClientInitialiser(
    private val client: Client,
    private val protocol: NetProtocol
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        ch.config().setOption(ChannelOption.TCP_NODELAY, false)
        ch.config().setOption(ChannelOption.SO_REUSEADDR, true)
        ch.config().setOption(ChannelOption.IP_TOS, 0x18)

        val p = ch.pipeline()
        p.addLast("slicer", PacketSlicingHandler())
        p.addLast("decoder", PacketCodecHandler(protocol))
        p.addLast("handler", ClientListener(client, protocol.createClientChannelHandler()))

        protocol.registerClient()
    }

    class ClientListener(
        private val client: Client,
        private val listener: ClientEventListener
    ) : SimpleChannelInboundHandler<Packet>() {
        override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
            listener.onPacketReceived(client, ctx, msg)
        }

        override fun channelActive(ctx: ChannelHandlerContext) {
            super.channelActive(ctx)
            this.listener.onConnected(this.client)
        }

        override fun channelInactive(ctx: ChannelHandlerContext) {
            super.channelInactive(ctx)
            this.listener.onDisconnected(this.client)
        }
    }
}
