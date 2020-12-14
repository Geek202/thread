package me.geek.tom.thread.tests

import me.geek.tom.thread.api.util.Colour
import kotlin.test.Test
import kotlin.test.assertEquals

class TestColour {
    @Test
    fun testColourConversion() {
        println("Creating Colour object")
        val colA = Colour(128, 96, 8)
        println("Copying colour using toInt()")
        val colAInt = colA.toInt()
        val colB = Colour(colAInt)
        println("Asserting values are the same")
        assertEquals(128, colB.red, "Red")
        assertEquals(96, colB.green, "Green")
        assertEquals(8, colB.blue, "Blue")
    }
}
