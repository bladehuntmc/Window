package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Column as CoreColumn
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class Column(
    size: Size2 = Size2(),
) : CoreColumn<ItemStack>(size) {
    override var reservation: Reservation<ItemStack>? = null
}

@WindowDsl
inline fun ParentComponent<ItemStack>.column(
    size: Size2 = Size2(),
    block: @WindowDsl Column.() -> Unit
): Column = Column(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}