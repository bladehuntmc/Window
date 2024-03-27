package net.bladehunt.window.core.canvas.container

import net.bladehunt.window.core.canvas.Canvas
import net.bladehunt.window.core.canvas.Reserved
import net.bladehunt.window.core.util.Int2

interface Container<Pixel, R : Reserved<Pixel>> : Canvas<Pixel, R> {
    val padding: Padding<Pixel>

    override fun calculateSizes() {
        val first = reservations.values.firstOrNull() ?: return
        val size = first.size
        first.size = size.copy(
            x = if (size.flexX) this.size.x - padding.left - padding.right else size.x,
            y = if (size.flexY) this.size.y - padding.top - padding.bottom else size.y
        )
    }

    override fun build(): Map<Int2, Pixel> {
        return buildMap {
            val reservation = reservations.values.firstOrNull()?.limit() ?: return@buildMap

            val size = this@Container.size
            for (y in 0 until size.y) {
                for (x in 0 until size.x) {
                    val isPadding = x < padding.left || x >= size.x - padding.right ||
                            y < padding.top || y >= size.y - padding.bottom
                    if (isPadding) {
                        set(Int2(x, y), padding.item)
                    }
                }
            }

            reservation.pixelMap.forEach { (pos, pixel) ->
                set(pos.copy(x = padding.left + pos.x, y = padding.top + pos.y), pixel)
            }
        }
    }
}