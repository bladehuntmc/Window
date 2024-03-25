package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.util.Size2

open class Composite<Pixel>(override val size: Size2) : Canvas<Pixel> {
    override val reservations: LinkedHashMap<Sized, Reservation<Pixel>> = linkedMapOf()
    override fun calculateShapes() {
        reservations.forEach { (_, reservation) ->
            val size = reservation.size
            if (size.x is Size.Flex) size.x.amount = this@Composite.size.x.amount
            if (size.y is Size.Flex) size.y.amount = this@Composite.size.y.amount
        }
        reservations.forEach { (sized) ->
            if (sized is Canvas<*>) {
                (sized as Canvas<Pixel>).calculateShapes()
            }
        }
    }

    override fun composite(): Map<Int2, Pixel> = buildMap {
        reservations.forEach { (sized, reservation) ->
            if (sized is Canvas<*>) {
                reservation.pixelMap.putAll((sized as Canvas<Pixel>).composite())
            }

            reservation.limit().pixelMap.forEach { (pos, pixel) -> set(pos, pixel) }
        }
    }

    override fun toString(): String {
        return "Composite(size=$size, reservations=$reservations)"
    }
}