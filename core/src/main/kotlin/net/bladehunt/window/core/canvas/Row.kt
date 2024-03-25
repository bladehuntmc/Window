package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

open class Row<Pixel>(override val size: Size) : Canvas<Pixel> {
    override val reservations: LinkedHashMap<Sized, Reservation<Pixel>> = linkedMapOf()

    override fun composite(): Map<Int2, Pixel> {
        var filledCols = 0
        val flexItems = arrayListOf<Size.Flex>()

        // calculate flexes
        reservations.keys.forEach { sized ->
            val size = sized.size
            if (size is Size.Flex) {
                flexItems += size
                return@forEach
            }
            filledCols += sized.size.x
        }
        val remainingCols = size.x - filledCols
        var remainder = if (flexItems.size != 0) remainingCols % flexItems.size else 0

        flexItems.forEach { flex ->
            flex.x = remainingCols.floorDiv(flexItems.size) + if (remainder > 0) 1 else 0
            flex.y = size.y
            remainder -= 1
        }

        return buildMap {
            var rows = 0
            reservations.forEach { (sized, res) ->
                val reservation = res.limit(sized.size)
                reservation.pixelMap.forEach { (pos, pixel) ->
                    set(pos.copy(x = pos.x + rows), pixel)
                }
                rows += sized.size.x
            }
        }
    }
}