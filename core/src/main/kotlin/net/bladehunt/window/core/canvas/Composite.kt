package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Composite<Pixel, R : Reserved<Pixel>> : Canvas<Pixel, R> {
    override fun calculateSizes() {
        reservations.values.forEach {
            it.size = it.size.copy(
                x = if (it.size.flexX) this.size.x else it.size.x,
                y = if (it.size.flexY) this.size.y else it.size.y
            )
        }
        reservations.keys
            .filterIsInstance<Canvas<Pixel, R>>()
            .forEach(Canvas<Pixel, R>::calculateSizes)
    }

    override fun build(): Map<Int2, Pixel> {
        return buildMap {
            reservations.values.forEach { reservation ->
                reservation.limit().pixelMap.forEach { (pos, pixel) ->
                    set(pos, pixel)
                }
            }
        }
    }
}
