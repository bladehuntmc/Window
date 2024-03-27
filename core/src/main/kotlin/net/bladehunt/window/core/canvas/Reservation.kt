package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

data class Reservation<Pixel>(override var size: Size2, val pixelMap: MutableMap<Int2, Pixel> = mutableMapOf()) : Shape {
    fun clear() = pixelMap.clear()

    fun limit(): Reservation<Pixel> {
        return copy(pixelMap = pixelMap.filter { it.key.x < size.x && it.key.y < size.y }.toMutableMap())
    }
    fun limit(size2: Size2): Reservation<Pixel> {
        return Reservation(size2, pixelMap.filter { it.key.x < size2.x && it.key.y < size2.y }.toMutableMap())
    }
}