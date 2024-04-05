package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.Row as CoreRow
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.item.ItemStack

class Row(
    size: Size2 = Size2()
) : CoreRow<ItemStack>(size), InteractionHandler<MinestomInteraction> {
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
                    if (event.clickPos.x in offset..<nextOffset) {
                        break
                    }
                    component = nextComponent
                    offset = nextOffset
                }

                val childEvent = event.copy(
                    clickPos = event.clickPos.copy(x = event.clickPos.x - offset)
                )
                if (childEvent.clickPos.x >= component.size.x  || childEvent.clickPos.y >= component.size.y) return
                try {
                    @Suppress("UNCHECKED_CAST")
                    (component as? InteractionHandler<MinestomInteraction>)?.onEvent(childEvent)
                } catch (_: ClassCastException) {}
            }
        }
    }
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