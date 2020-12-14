package me.geek.tom.thread.api

/**
 * Custom [LedThread] implementations should have a constructor with this as a single parameter
 * in order to be compatible with the server provided in <code>thread-network-api-server-impl</code>
 */
data class LedThreadConfig(
    val config: Map<String, String>
)
