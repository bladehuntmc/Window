package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.item.ItemStack

class StaticItem(val item: ItemStack) : Component<ItemStack>, InteractionHandler<MinestomInteraction> {
    override var reservation: Reservation<ItemStack>? = null
    override val size: Size2 = Size2(1, 1)
    override fun preRender(limits: Int2) {}

    override fun render() {
        val reservation = reservation ?: return
        reservation[Int2(0, 0)] = item
    }
    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                event.result.isCancel = true
            }
        }
    }
}

@WindowDsl
fun ParentComponent<ItemStack>.staticItem(item: ItemStack): StaticItem = StaticItem(item).also {
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}