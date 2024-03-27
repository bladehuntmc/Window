package net.bladehunt.window.core.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.canvas.Reserved
import net.bladehunt.window.core.canvas.Shape
import net.bladehunt.window.core.util.Size2

interface Component<Pixel> : ReactiveContext, Shape, Reserved<Pixel> {
    override val size: Size2
        get() = reservation.size

    fun render()

    override fun onEvent(event: Event) = render()
    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}
}
