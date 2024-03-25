package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

data class Reservation<Pixel>(val pixelMap: MutableMap<Int2, Pixel> = hashMapOf()) {
    fun limit(size: Size): Reservation<Pixel> {
        val (x, y) = size
        return Reservation(pixelMap.filter { it.key.x < x && it.key.y < y }.toMutableMap())
    }
}