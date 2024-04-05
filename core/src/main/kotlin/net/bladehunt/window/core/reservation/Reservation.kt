package net.bladehunt.window.core.reservation

import net.bladehunt.window.core.util.Int2

interface Reservation<Pixel> : Iterable<Pair<Int2, Pixel>> {
    operator fun set(slot: Int2, pixel: Pixel)
    operator fun get(slot: Int2): Pixel?

    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean

    fun clear()
}