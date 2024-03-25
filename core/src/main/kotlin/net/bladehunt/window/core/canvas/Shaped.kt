package net.bladehunt.window.core.canvas

interface Shaped {
    val size: Size

    fun <Pixel> reservation(): Reservation<Pixel> = Reservation(size)
}