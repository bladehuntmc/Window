package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.Column as CoreColumn
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.item.ItemStack

class Column(
    size: Size2 = Size2(),
) : CoreColumn<ItemStack>(size), InteractionHandler<MinestomInteraction> {
    override var reservation: Reservation<ItemStack>? = null
    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                val iter = offsets.toList().sortedBy { it.second }.iterator()
                val first = iter.next()

                var offset = first.second
                var component: Component<ItemStack> = first.first

                while (iter.hasNext()) {
                    val (nextComponent, nextOffset) = iter.next()
                    if (event.clickPos.y in offset..<nextOffset) {
                        break
                    }
                    component = nextComponent
                    offset = nextOffset
                }

                val childEvent = event.copy(
                    clickPos = event.clickPos.copy(y = event.clickPos.y - offset)
                )
                if (childEvent.clickPos.y >= component.size.y  || childEvent.clickPos.x >= component.size.x) return
                try {
                    @Suppress("UNCHECKED_CAST")
                    (component as? InteractionHandler<MinestomInteraction>)?.onEvent(childEvent)
                } catch (_: ClassCastException) {}
            }
        }
    }
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