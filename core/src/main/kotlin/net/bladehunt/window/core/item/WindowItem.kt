package net.bladehunt.window.core.item

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.platform.NativeHandleContainer
import net.bladehunt.window.core.render.Renderable

interface WindowItem<NativeType> : NativeHandleContainer<NativeType>, ReactiveContext, Renderable {
    val displayName: AdventureComponent
    val lore: MutableList<AdventureComponent>
    var amount: Int

    override fun onEvent(event: Event) {
        render()
    }

    override fun onSubscribe(publisher: EventPublisher) {}

    override fun onUnsubscribe(publisher: EventPublisher) {}
}