package net.bladehunt.window.core

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.canvas.Canvas
import net.bladehunt.window.core.canvas.Column
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2

open class Window<Pixel>(size: Size2) : Column<Pixel>(size), ParentComponent<Pixel> {
    override val children: MutableCollection<Component<Pixel>> = arrayListOf()

    fun render() = render(this)
    override fun render(canvas: Canvas<Pixel>) {
        children.forEach {
            it.render(canvas)
        }
        calculateShapes()
        children.forEach {
            it.render(canvas)
        }
    }

    override fun onEvent(event: Event) = render(this)
    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}
}