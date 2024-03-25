package net.bladehunt.window.minestom.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.canvas.Canvas
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomWindow
import net.minestom.server.item.ItemStack
import net.bladehunt.window.core.canvas.Column as CanvasColumn

class Column(size: Size2) : ParentComponent<ItemStack>, CanvasColumn<ItemStack>(size) {
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
inline fun ParentComponent<ItemStack>.column(
    size: Size2 = Size2(),
    block: @WindowDsl Column.() -> Unit
): Column = Column(size).also {
    this.children.add(it)
    it.block()
}