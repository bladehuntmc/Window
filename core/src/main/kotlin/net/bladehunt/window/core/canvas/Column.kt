package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.util.Size2

open class Column<Pixel>(override val size: Size2) : Canvas<Pixel> {
    override val reservations: LinkedHashMap<Sized, Reservation<Pixel>> = linkedMapOf()

    override fun calculateShapes() {
        var filledRows = 0
        val flexItems = arrayListOf<Size.Flex>()

        reservations.forEach { (_, reservation) ->
            val (sizeX, sizeY) = reservation.size
            if (sizeX is Size.Flex) sizeX.amount = size.x.amount
            if (sizeY is Size.Flex) {
                flexItems += sizeY
                return@forEach
            }
            filledRows += sizeY.amount
        }
        val remainingRows = size.y.amount - filledRows
        var remainder = if (flexItems.size != 0) remainingRows % flexItems.size else 0

        flexItems.forEach { flex ->
            flex.amount = remainingRows.floorDiv(flexItems.size) + if (remainder > 0) 1 else 0
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
                    set(pos.copy(y = pos.y + rows), pixel)
                }
                rows += reservation.size.y.amount
            }
        }
    }

    override fun toString(): String {
        return "Column(size=$size, reservations=$reservations)"
    }
}