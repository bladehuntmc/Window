package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.canvas.Row as CanvasRow
import net.minestom.server.item.ItemStack

class Row(
    override val reservation: Reservation<ItemStack>
) : ParentComponent<ItemStack>, CanvasRow<ItemStack, Component<ItemStack>> {
    override val size: Size2
        get() = reservation.size

    override val reservations: MutableMap<Component<ItemStack>, Reservation<ItemStack>> = linkedMapOf()
    override fun reserve(reserved: Component<ItemStack>) {
        reservations[reserved] = reserved.reservation
    }

    override fun composite() {
        calculateSizes()
        reservation.clear()
        build().forEach { (pos, item) ->
            reservation.pixelMap[pos] = item
        }
    }
    override fun render() {
        iterator().forEach {
            it.render()
        }
        composite()
    }

    override fun clear() = reservations.clear()
    override fun iterator(): Iterator<Component<ItemStack>> = reservations.keys.iterator()
    override fun removeChild(child: Component<ItemStack>): Boolean = reservations.remove(child) != null
    override fun addChild(child: Component<ItemStack>): Boolean {
        reserve(child)
        return true
    }
    override fun toString(): String {
        return "Row(size=$size, reservations=$reservations)"
    }
}
@WindowDsl
inline fun ParentComponent<ItemStack>.row(
    size: Size2 = Size2(),
    block: @WindowDsl Row.() -> Unit
): Row = Row(Reservation(size)).also {
    this.addChild(it)
    it.block()
}