package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Row as CoreRow
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class Row(
    size: Size2 = Size2()
) : CoreRow<ItemStack>(size) {
    override var reservation: Reservation<ItemStack>? = null
}

@WindowDsl
inline fun ParentComponent<ItemStack>.row(
    size: Size2 = Size2(),
    block: @WindowDsl Row.() -> Unit
): Row = Row(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}