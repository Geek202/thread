package me.geek.tom.thread.netlib.api.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import me.geek.tom.thread.netlib.api.netty.ServerInitialiser
import me.geek.tom.thread.netlib.api.proto.NetProtocol
import java.util.function.Supplier

//class ConnectionManager(
//    private val protocolSupplier: Supplier<NetProtocol>,
//) {
//    fun createChannelInitialiser(): ChannelInitializer<SocketChannel> {
//        val proto = protocolSupplier.get()
//        return ServerInitialiser(proto)
//    }
//}
