package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class Fill(val itemStack: ItemStack, override val reservation: Reservation<ItemStack>) : Component<ItemStack> {
    override fun render() {
        val pixelMap = reservation.pixelMap
        for (x in 0..<size.x) {
            for (y in 0..<size.y) {
                pixelMap[Int2(x,y)] = itemStack
            }
        }
    }

    override fun toString(): String {
        return "Fill(itemStack=$itemStack, size=$size)"
    }
}
@WindowDsl
fun ParentComponent<ItemStack>.fill(
    item: ItemStack
): Fill = Fill(item, Reservation(Size2())).also {
    this.addChild(it)
}