package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

data class Reservation<Pixel>(val value: Any, val size: Int2) {
    val pixels = hashMapOf<Int2, Pixel>()
}