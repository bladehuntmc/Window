package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class Empty(override val size: Size2, override val reservation: Reservation<ItemStack>) : Component<ItemStack> {
    override fun render() {}
    override fun toString(): String {
        return "Empty(size=$size)"
    }
}
@WindowDsl
fun ParentComponent<ItemStack>.empty(
    size: Size2 = Size2()
): Empty = Empty(size, Reservation(size)).also {
    addChild(it)
}