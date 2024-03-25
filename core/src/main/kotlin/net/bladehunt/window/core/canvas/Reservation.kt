package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

data class Reservation<Pixel>(override val size: Size2, val pixelMap: MutableMap<Int2, Pixel> = hashMapOf()) : Sized {
    fun limit(): Reservation<Pixel> {
        return copy(pixelMap = pixelMap.filter { it.key.x < size.x.amount && it.key.y < size.y.amount }.toMutableMap())
    }
    fun limit(size2: Size2): Reservation<Pixel> {
        val (x, y) = size2
        return Reservation(size2, pixelMap.filter { it.key.x < x.amount && it.key.y < y.amount }.toMutableMap())
    }
}