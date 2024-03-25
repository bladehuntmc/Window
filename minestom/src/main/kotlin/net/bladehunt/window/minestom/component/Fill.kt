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
import net.minestom.server.item.ItemStack

class Fill(val itemStack: ItemStack) : ParentComponent<ItemStack>, Sized {
    override val children: MutableCollection<Component<ItemStack>> = arrayListOf()

    override fun render(canvas: Canvas<ItemStack>) {
        val (_, pixelMap) = canvas.reserve(this)
        println(size)
        for (x in 0..<size.x.amount) {
            for (y in 0..<size.y.amount) {
                pixelMap[Int2(x,y)] = itemStack
            }
        }
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

    override val size: Size2 = Size2()
}
@WindowDsl
fun ParentComponent<ItemStack>.fill(
    item: ItemStack
): Fill = Fill(item).also {
    this.children.add(it)
}