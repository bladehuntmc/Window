package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Column<Pixel, R : Reserved<Pixel>> : Canvas<Pixel, R> {
    override fun calculateSizes() {
        var filledRows = 0
        val flexItems = arrayListOf<Reservation<Pixel>>()

        reservations.forEach { (_, reservation) ->
            val (_, flexX, sizeY, flexY) = reservation.size
            if (flexX) reservation.size = reservation.size.copy(x = this.size.x)
            if (flexY) {
                flexItems.add(reservation)
                return@forEach
            }
            filledRows += sizeY
        }
        val remainingRows = size.y - filledRows
        var remainder = if (flexItems.size != 0) remainingRows % flexItems.size else 0

        flexItems.forEach { reservation ->
            reservation.size = reservation.size.copy(
                y = remainingRows.floorDiv(flexItems.size) + if (remainder > 0) 1 else 0
            )
            remainder -= 1
        }

        reservations.keys
            .filterIsInstance<Canvas<Pixel, R>>()
            .forEach(Canvas<Pixel, R>::calculateSizes)
    }

    override fun build(): Map<Int2, Pixel> {
        return buildMap {
            var rows = 0
            reservations.values.forEach { reservation ->
                val limited = reservation.limit()
                limited.pixelMap.forEach { (pos, pixel) ->
                    set(pos.copy(y = pos.y + rows), pixel)
                }
                rows += reservation.size.y
            }
        }
    }
}