package net.bladehunt.window.core.reservation

import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Int2

class ChildReservation<Pixel>(
    private val component: Component<Pixel>,
    private val onSet: (pos: Int2, pixel: Pixel) -> Unit
) : Reservation<Pixel> {

    constructor(component: Component<Pixel>, parent: ParentComponent<Pixel>) : this(
        component,
        { pos: Int2, pixel: Pixel ->
            parent.updateOne(component, pos, pixel)
        }
    )

    private val pixelMap = hashMapOf<Int2, Pixel>()
    override fun set(slot: Int2, pixel: Pixel) {
        pixelMap[slot] = pixel
        onSet(slot, pixel)
    }

    override fun get(slot: Int2): Pixel? = pixelMap[slot]

    override fun isEmpty(): Boolean = pixelMap.isEmpty()

    override fun isNotEmpty(): Boolean = pixelMap.isNotEmpty()

    override fun clear() = pixelMap.clear()

    override fun iterator(): Iterator<Pair<Int2, Pixel>> = pixelMap.map { it.toPair() }.iterator()
}