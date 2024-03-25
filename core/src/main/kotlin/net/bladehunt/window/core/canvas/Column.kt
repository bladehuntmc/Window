package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

open class Column<Pixel>(override val size: Size) : Canvas<Pixel>, Shaped {
    override val reservations: MutableList<Pair<Shaped, Reservation<Pixel>>> = arrayListOf()

    override fun composite(): Map<Int2, Pixel> {
        val pixelMap = hashMapOf<Int2, Pixel>()
        var occupiedRows = 0

        reservations.forEach { (_, reservation) ->
            occupiedRows += reservation.size.y

            reservation.pixelMap.forEach { (pos, pixel) ->
                if (pos.copy(y = pos.y + occupiedRows) !in size) return@forEach

                pixelMap[Int2(pos.x, pos.y + occupiedRows)] = pixel
            }
        }

        return pixelMap
    }
}