package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Canvas<Pixel, R : Reserved<Pixel>> : Shape {
    val reservations: Map<R, Reservation<Pixel>>

    fun reserve(reserved: R)
    fun calculateSizes()

    fun build(): Map<Int2, Pixel>
    fun composite()
}