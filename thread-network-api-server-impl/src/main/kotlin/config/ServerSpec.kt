package me.geek.tom.thread.api.network.server.config

import com.uchuhimo.konf.ConfigSpec

object ServerSpec : ConfigSpec() {

    val host by required<String>()
    val port by required<Int>()

}