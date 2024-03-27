package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.util.set
import net.minestom.server.item.ItemStack

class StaticItem(val itemStack: ItemStack, override val reservation: Reservation<ItemStack>) : Component<ItemStack> {
    override fun render() {
        reservation.pixelMap[0, 0] = itemStack
    }

    override fun toString(): String {
        return "StaticItem(itemStack=${itemStack.material()}, size=$size)"
    }

    override val size: Size2 = Size2(1, 1)
}
@WindowDsl
fun ParentComponent<ItemStack>.staticItem(
    item: ItemStack
): StaticItem = StaticItem(item, Reservation(Size2(1, 1))).also {
    addChild(it)
}