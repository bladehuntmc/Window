package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.canvas.container.Padding
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.canvas.container.Container as CanvasContainer
import net.minestom.server.item.ItemStack

class Container(
    override val padding: Padding<ItemStack>,
    override val reservation: Reservation<ItemStack>,
) : ParentComponent<ItemStack>, CanvasContainer<ItemStack, Component<ItemStack>> {
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
        return "Container(size=$size, padding=$padding, reservations=$reservations)"
    }
}
@WindowDsl
inline fun ParentComponent<ItemStack>.container(
    size: Size2 = Size2(),
    padding: Padding<ItemStack>,
    block: @WindowDsl Container.() -> Unit
): Container = Container(padding, Reservation(size)).also {
    this.addChild(it)
    it.block()
}