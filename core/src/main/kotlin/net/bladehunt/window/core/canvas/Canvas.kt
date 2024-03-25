package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Canvas<Pixel> {
    val reservations: MutableList<Pair<Shaped, Reservation<Pixel>>>

    fun reserve(shaped: Shaped, index: Int = -1): Reservation<Pixel> {
        val reservation = shaped.reservation<Pixel>()

        when (index) {
            -1 -> {
                reservations.add(shaped to reservation)
            }
            else -> {
                reservations.add(index, shaped to reservation)
            }
        }

        return reservation
    }

    fun composite(): Map<Int2, Pixel>
}