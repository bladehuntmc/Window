package net.bladehunt.window.minestom.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Canvas
import net.bladehunt.window.core.canvas.Sized
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomWindow
import net.minestom.server.item.ItemStack
import net.bladehunt.window.core.canvas.Column as CanvasColumn

class StaticItem(val itemStack: ItemStack) : ParentComponent<ItemStack>, Sized {
    override val children: MutableCollection<Component<ItemStack>> = arrayListOf()

    override fun render(canvas: Canvas<ItemStack>) {
        val (_, pixelMap) = canvas.reserve(this)
        pixelMap[Int2(0, 0)] = itemStack
    }

    override fun onEvent(event: Event) {
    }

    override fun onSubscribe(publisher: EventPublisher) {
    }

    override fun onUnsubscribe(publisher: EventPublisher) {
    }

    override fun toString(): String {
        return "StaticItem(itemStack=$itemStack, children=$children, size=$size)"
    }

    override val size: Size2 = Size2(1, 1)
}
@WindowDsl
fun ParentComponent<ItemStack>.staticItem(
    item: ItemStack
): StaticItem = StaticItem(item).also {
    this.children.add(it)
}