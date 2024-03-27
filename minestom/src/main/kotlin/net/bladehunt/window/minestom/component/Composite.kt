package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.canvas.Composite as CanvasComposite
import net.minestom.server.item.ItemStack

class Composite(
    override val reservation: Reservation<ItemStack>
) : ParentComponent<ItemStack>, CanvasComposite<ItemStack, Component<ItemStack>> {
    override val reservations: MutableMap<Component<ItemStack>, Reservation<ItemStack>> = linkedMapOf()
    override fun reserve(reserved: Component<ItemStack>) {
        reservations[reserved] = reserved.reservation
    }

    override fun composite() {
        reservation.clear()
        build().forEach { (pos, item) ->
            reservation.pixelMap[pos] = item
        }
    }
    override fun render() {
        calculateSizes()
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
        return "Composite(size=$size, reservations=$reservations)"
    }
}
@WindowDsl
inline fun ParentComponent<ItemStack>.composite(
    size: Size2 = Size2(),
    block: @WindowDsl Composite.() -> Unit
): Composite = Composite(Reservation(size)).also {
    this.addChild(it)
    it.block()
}