package net.bladehunt.window.core.dsl

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.EventSubscriber
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext

/**
 * A reactive builder - Automatically unsubscribes from publishers and allows subscribing to all collected publishers via the subscribe function.
 */
abstract class Builder<T> : ReactiveContext {
    private val publishers = mutableListOf<EventPublisher>()

    override fun onEvent(event: Event) {
        throw UnsupportedOperationException("Events are not supposed to be handled in the DSL builders.")
    }
    override fun onSubscribe(publisher: EventPublisher) {
        publishers.add(publisher)
        publisher.unsubscribe(this)
    }
    override fun onUnsubscribe(publisher: EventPublisher) {}

    /**
     * Be sure to call this when finished building the item.
     */
    fun subscribe(subscriber: EventSubscriber) {
        publishers.forEach { it.subscribe(subscriber) }
    }

    abstract fun build(): T
}