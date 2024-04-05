package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Container as CoreContainer
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.decoration.Background
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

@Suppress("UNCHECKED_CAST")
class Container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR)
) : CoreContainer<ItemStack>(size, background, padding) {
    override var reservation: Reservation<ItemStack>? = null
}

@Suppress("UNCHECKED_CAST")
@WindowDsl
inline fun ParentComponent<ItemStack>.container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR),
    block: @WindowDsl Container.() -> Unit
): Container = Container(
    size,
    background,
    padding
)
    .also {
        it.block()
        it.reservation = ChildReservation(it, this)
        this.addChild(it)
    }