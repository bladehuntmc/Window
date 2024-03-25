package net.bladehunt.window.minestom.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Canvas
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack
import net.bladehunt.window.core.canvas.Row as CanvasRow

class Row(size: Size2) : ParentComponent<ItemStack>, CanvasRow<ItemStack>(size) {
    override val children: MutableCollection<Component<ItemStack>> = arrayListOf()

    override fun render(canvas: Canvas<ItemStack>) {
        canvas.reserve(this)
        children.forEach {
            it.render(this)
        }
    }

    override fun onEvent(event: Event) {
    }

    override fun onSubscribe(publisher: EventPublisher) {
    }

    override fun onUnsubscribe(publisher: EventPublisher) {
    }
}
@WindowDsl
inline fun ParentComponent<ItemStack>.row(
    size: Size2 = Size2(),
    block: @WindowDsl Row.() -> Unit
): Row = Row(size).also {
    this.children.add(it)
    it.block()
}