package net.bladehunt.window.core.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.reservation.Reserved
import net.bladehunt.window.core.Shape
import net.bladehunt.window.core.util.Int2

interface Component<Pixel> : ReactiveContext, Shape, Reserved<Pixel> {
    fun preRender(limits: Int2)
    fun render()

    override fun onEvent(event: Event) = render()
    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}
}
