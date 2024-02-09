package net.bladehunt.window.core.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.Window
import net.bladehunt.window.core.render.Renderable
import net.bladehunt.window.core.util.Tree

interface Component : Renderable, ReactiveContext {
    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}
}

interface NodeComponent : Component {
    /**
     * Returns null if the node cannot be found.
     */
    fun getNode(): Tree.Node<Component>?
}

interface WindowComponent<W : Window> : NodeComponent {
    val window: W

    override fun onEvent(event: Event) {
        val node = getNode() ?: run {
            render()
            return
        }
        window.renderChildren(node)
    }
}