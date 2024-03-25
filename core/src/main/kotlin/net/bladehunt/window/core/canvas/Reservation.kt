package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

data class Reservation<Pixel>(override val size: Size) : Shaped {
    val pixelMap: MutableMap<Int2, Pixel> = hashMapOf()

    operator fun set(pos: Int2, pixel: Pixel) {
        pixelMap[pos] = pixel
    }
    operator fun set(x: Int, y: Int, pixel: Pixel) = set(Int2(x, y), pixel)

    operator fun get(pos: Int2): Pixel? = pixelMap[pos]
    operator fun get(x: Int, y: Int): Pixel? = get(Int2(x, y))

    operator fun contains(pos: Int2): Boolean = pixelMap.contains(pos)
    operator fun contains(pos: Pair<Int, Int>): Boolean = contains(Int2(pos.first, pos.second))
}