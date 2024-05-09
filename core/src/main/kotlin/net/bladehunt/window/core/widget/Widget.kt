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

package net.bladehunt.window.core.widget

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.Sized
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.render.RenderContext
import net.bladehunt.window.core.util.Int2
import java.util.WeakHashMap

abstract class Widget<T> : Sized, ReactiveContext {
    private val updateHandlers: WeakHashMap<Any, () -> Unit> = WeakHashMap()

    fun setUpdateHandler(any: Any, handler: () -> Unit) {
        updateHandlers[any] = handler
    }

    open fun requestUpdate() {
        updateHandlers.values.forEach { it() }
    }

    abstract fun onRender(layer: Layer<T>, context: RenderContext<T>): Int2

    @JvmOverloads
    fun render(layer: Layer<T>, context: RenderContext<T>, force: Boolean = false): Int2 {
        val cache = context.cache.search(context.path)
        val size = cache?.size
        if (cache == null || size == null || force) {
            layer.clear()
            val newContext = context.copy(path = listOf(*context.path.toTypedArray(), this))
            val finalSize = onRender(layer, newContext)
            context.cache.cache(context.path, layer, finalSize)

            var invalidate = false
            cache?.parent?.children?.forEach { node ->
                if (invalidate) {
                    node.size = null
                }
                if (node.widget == this) invalidate = true
            }
            return finalSize
        }
        cache.layer?.forEach { (pos, pixel) ->
            layer[pos] = pixel
        }
        return size
    }

    override fun onSubscribe(publisher: EventPublisher) {}
    override fun onUnsubscribe(publisher: EventPublisher) {}

    override fun onEvent(event: Event) {
        requestUpdate()
    }
}