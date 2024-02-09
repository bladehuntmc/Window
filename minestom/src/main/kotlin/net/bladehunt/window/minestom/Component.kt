package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.reactivity.ReactiveContext

interface Component : ReactiveContext {
    fun render()

    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}
}

interface ChildComponent : Component {
    val window: Window
}