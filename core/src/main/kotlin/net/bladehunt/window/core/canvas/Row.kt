package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

interface Row<Pixel, R : Reserved<Pixel>> : Canvas<Pixel, R> {
    override fun calculateSizes() {
        var filledColumns = 0
        val flexItems = arrayListOf<Reservation<Pixel>>()

        reservations.forEach { (_, reservation) ->
            val (sizeX, flexX, _, flexY) = reservation.size
            if (flexY) reservation.size = reservation.size.copy(y = this.size.y)
            if (flexX) {
                flexItems.add(reservation)
                return@forEach
            }
            filledColumns += sizeX
        }
        val remainingColumns = size.x - filledColumns
        var remainder = if (flexItems.size != 0) remainingColumns % flexItems.size else 0

        flexItems.forEach { reservation ->
            reservation.size = reservation.size.copy(
                x = remainingColumns.floorDiv(flexItems.size) + if (remainder > 0) 1 else 0
            )
            remainder -= 1
        }

        reservations.keys
            .filterIsInstance<Canvas<Pixel, R>>()
            .forEach(Canvas<Pixel, R>::calculateSizes)
    }

    override fun build(): Map<Int2, Pixel> {
        return buildMap {
            var columns = 0
            reservations.values.forEach { reservation ->
                reservation.limit().pixelMap.forEach { (pos, pixel) ->
                    set(pos.copy(x = pos.x + columns), pixel)
                }
                columns += reservation.size.x
            }
        }
    }
}
