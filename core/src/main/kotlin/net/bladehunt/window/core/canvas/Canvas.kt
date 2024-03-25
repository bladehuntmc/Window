package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Canvas<Pixel> : Sized {
    val reservations: LinkedHashMap<Sized, Reservation<Pixel>>

    fun reserve(sized: Sized): Reservation<Pixel> = Reservation<Pixel>().also { reservations[sized] = it }
    fun composite(): Map<Int2, Pixel>
}