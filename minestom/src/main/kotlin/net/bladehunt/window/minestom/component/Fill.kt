package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class Fill(val item: ItemStack) : Component<ItemStack> {
    override var reservation: Reservation<ItemStack>? = null
    override var size: Size2 = Size2()
    override fun preRender(limits: Int2) {
        size = size.copy(x = limits.x, y = limits.y)
    }

    override fun render() {
        println("Rendering fill")
        val reservation = reservation ?: return
        for (x in 0..<size.x) {
            for (y in 0..<size.y) {
                reservation[Int2(x, y)] = item
            }
        }
    }
}

@WindowDsl
fun ParentComponent<ItemStack>.fill(item: ItemStack): Fill = Fill(item).also {
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}