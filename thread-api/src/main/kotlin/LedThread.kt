package me.geek.tom.thread.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.geek.tom.thread.api.util.Colour


interface LedThread : AutoCloseable {
    /**
     * IMPLEMENTATION NOTE: This method should not update the displayed representation of the strip until [render] is called
     *
     * @param pixel The index of the pixel on the strip. Expected to be 0-indexed.
     * @param colour The colour to set the pixel to
     */
    fun setPixel(pixel: Int, colour: Colour)

    /**
     * The default implementation of this constructs a new [Colour] and calls [setPixel] with it.
     *
     * @see setPixel
     */
    fun setPixel(pixel: Int, r: Int, g: Int, b: Int) = setPixel(pixel, Colour(r, g, b))

    /**
     * Sets the entire thread to be [col].
     *
     * The default implementation is a simple loop to [getLength] that sets all the pixels,
     * and it is suggested that a native fill approach should be used by implementors.
     */
    fun fillThread(col: Colour) {
        for (i in 0 until getLength()) {
            this.setPixel(i, col)
        }
    }

    /**
     * The default implementation of this creates a new [Colour] and delegates to [fillThread]
     */
    fun fillThread(r: Int, g: Int, b: Int) = fillThread(Colour(r, g, b))

    /**
     * Displays the state of the pixels on the strip.
     * This is where any IO should happen, not in any of the [setPixel] methods.
     */
    suspend fun render()

    /**
     * @return The length of this strip, in pixels.
     */
    fun getLength(): Int

    /**
     * Release any resources that may have been opened by this strip.
     * This will be called from [close] by default
     */
    suspend fun release()

    override fun close() {
        GlobalScope.launch {
            release()
        }
    }
}
