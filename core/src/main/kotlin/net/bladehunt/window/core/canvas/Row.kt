package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.util.Size2

open class Row<Pixel>(override val size: Size2) : Canvas<Pixel> {
    override val reservations: LinkedHashMap<Sized, Reservation<Pixel>> = linkedMapOf()
    override fun calculateShapes() {
        var filledCols = 0
        val flexItems = arrayListOf<Size.Flex>()

        reservations.forEach {  (_, reservation) ->
            val (sizeX, sizeY) = reservation.size
            if (sizeY is Size.Flex) sizeY.amount = size.y.amount
            if (sizeX is Size.Flex) {
                flexItems += sizeX
                return@forEach
            }
            filledCols += sizeX.amount
        }
        val remainingCols = size.x.amount - filledCols
        var remainder = if (flexItems.size != 0) remainingCols % flexItems.size else 0

        flexItems.forEach { flex ->
            flex.amount = remainingCols.floorDiv(flexItems.size) + if (remainder > 0) 1 else 0
            remainder -= 1
        }

        reservations.forEach { (sized) ->
            if (sized is Canvas<*>) {
                (sized as Canvas<Pixel>).calculateShapes()
            }
        }
    }
    override fun composite(): Map<Int2, Pixel> {
        return buildMap {
            var rows = 0
            reservations.forEach { (sized, reservation) ->
                if (sized is Canvas<*>) {
                    reservation.pixelMap.putAll((sized as Canvas<Pixel>).composite())
                }
                reservation.limit().pixelMap.forEach { (pos, pixel) ->
                    set(pos.copy(x = pos.x + rows), pixel)
                }
                rows += reservation.size.x.amount
            }
        }
    }

    override fun toString(): String {
        return "Row(size=$size, reservations=$reservations)"
    }
}