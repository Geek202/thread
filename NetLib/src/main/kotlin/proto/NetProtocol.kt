package me.geek.tom.thread.netlib.api.proto

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import me.geek.tom.thread.netlib.api.listener.ClientEventListener
import me.geek.tom.thread.netlib.api.listener.ServerEventListener

/**
 * Defines a protocol for data transfer over the connection.
 */
abstract class NetProtocol {

    private val incoming: Int2ObjectOpenHashMap<Class<out Packet>> = Int2ObjectOpenHashMap()
    private val outgoing: Object2ObjectOpenHashMap<Class<out Packet>, Int> = Object2ObjectOpenHashMap()

    /**
     * Called as a this protocol is initialised on the [me.geek.tom.thread.netlib.api.server.Server]
     */
    abstract fun registerServer()

    /**
     * Called as a this protocol is initialised on the [me.geek.tom.thread.netlib.api.client.Client]
     */
    abstract fun registerClient()

    /**
     * Return a [ServerEventListener] to listen for events on the server.
     */
    abstract fun createServerChannelHandler(): ServerEventListener

    /**
     * Return a [ClientEventListener] to listen for events on the client.
     */
    abstract fun createClientChannelHandler(): ClientEventListener

    /**
     * Registers a new packet that can be incoming and outgoing.
     */
    fun register(id: Int, klass: Class<out Packet>) {
        this.registerIncoming(id, klass)
        this.registerOutgoing(klass, id)
    }

    /**
     * Register a [Packet] that can be received from the remote connection.
     */
    fun registerIncoming(id: Int, klass: Class<out Packet>) {
        this.incoming[id] = klass
        try {
            this.createIncoming(id)
        } catch (e: IllegalStateException) {
            this.incoming.remove(id)
            throw IllegalArgumentException("Invalid packet with id: $id", e)
        }
    }

    /**
     * Register a [Packet] that can be send to the remote connection.
     */
    fun registerOutgoing(klass: Class<out Packet>, id: Int) {
        this.outgoing[klass] = id
    }

    /**
     * Called as a packet is received, to begin the decoding process.
     */
    fun createIncoming(id: Int): Packet {
        val cls = this.incoming[id]?: throw IllegalArgumentException("Invalid packet id: $id")
        try {
            val constructor = cls.getDeclaredConstructor()
            if (!constructor.isAccessible) constructor.isAccessible = true
            return constructor.newInstance()
        } catch (e: NoSuchMethodError) {
            throw IllegalStateException("Packet with id: $id (${cls.name}) should have a 0 args constructor for construction!")
        } catch (e: Exception) {
            throw IllegalStateException("Failed to create packet with $id (${cls.name})", e)
        }
    }

    /**
     * Gets the ID of an outgoing [Packet] that is being sent.
     */
    fun getOutgoingId(msg: Packet): Int {
        return this.outgoing[msg::class.java] ?: throw IllegalArgumentException("Unregistered packet class: ${msg::class.java.name}")
    }
}
