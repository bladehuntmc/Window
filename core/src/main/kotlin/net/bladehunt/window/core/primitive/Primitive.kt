/*
 * Copyright 2024 BladehuntMC
 * Copyright 2024 oglassdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package net.bladehunt.window.core.primitive

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.ReactiveProperty
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.widget.Sized
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

enum class Behavior {
    NONE,
    PARTIAL_RENDER,
    FULL_RENDER,
    PARTIAL_BUILD,
    FULL_BUILD
}

abstract class Primitive<T> : Sized {

    private val handledProperties: ArrayList<Property<*>> = arrayListOf()

    fun registerHandlers(node: Window.Node<T>) {
        handledProperties.forEach { property -> property.handleNode(node) }
    }

    fun unregisterHandlers(node: Window.Node<T>) {
        handledProperties.forEach { property -> property.dispose(node) }
    }

    abstract fun build(parent: Window.Node<T>)

    abstract fun render(node: Window.Node<T>)

    @JvmSynthetic
    fun <V> property(
        behavior: Behavior = Behavior.PARTIAL_RENDER
    ): Property<V?> = Property<V?>(null, behavior).also(handledProperties::add)

    @JvmSynthetic
    fun <V> property(
        default: V,
        behavior: Behavior = Behavior.PARTIAL_RENDER
    ): Property<V> = Property(default, behavior).also(handledProperties::add)


    inner class Property<T>(default: T, val behavior: Behavior = Behavior.PARTIAL_RENDER) :
        ReactiveContext, ReadWriteProperty<Primitive<*>, T> {

        private val handlers: WeakHashMap<Window.Node<*>, () -> Unit> = WeakHashMap()

        var value: T = default

        fun <P> handleNode(node: Window.Node<P>) {
            handlers[node] =
                when (behavior) {
                    Behavior.NONE -> {
                        {}
                    }
                    Behavior.PARTIAL_RENDER -> {
                        { node.primitive.render(node) }
                    }
                    Behavior.FULL_RENDER -> {
                        {
                            val root = node.getRoot()
                            root.primitive.render(root)
                        }
                    }
                    Behavior.PARTIAL_BUILD -> {
                        {}
                    }
                    Behavior.FULL_BUILD -> {
                        {}
                    }
                }
        }

        fun <P> dispose(node: Window.Node<P>) {
            handlers.remove(node)
        }

        override fun onEvent(event: Event) {
            handlers.values.forEach { it() }
        }

        inline fun reactive(block: ReactiveProperty<T>) {
            value = block(this)
        }

        override fun onSubscribe(publisher: EventPublisher) {}

        override fun onUnsubscribe(publisher: EventPublisher) {}

        override fun getValue(thisRef: Primitive<*>, property: KProperty<*>): T = value

        override fun setValue(thisRef: Primitive<*>, property: KProperty<*>, value: T) {
            this.value = value
        }
    }

}