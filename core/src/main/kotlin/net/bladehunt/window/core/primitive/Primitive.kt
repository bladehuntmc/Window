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

import net.bladehunt.window.core.Context
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.widget.Resizable

abstract class Primitive<T> : Resizable {
    private val handledProperties: ArrayList<WidgetProperty<*>> = arrayListOf()

    override var size by property(Size(1, 1), WidgetProperty.Behavior.FULL_RENDER)

    fun registerHandlers(node: Window.Node<T>) {
        handledProperties.forEach { property -> property.handleNode(node) }
    }

    fun unregisterHandlers(node: Window.Node<T>) {
        handledProperties.forEach { property -> property.dispose(node) }
    }

    abstract fun buildNode(parent: Window.Node<T>, context: Context)

    abstract fun render(node: Window.Node<T>)

    @JvmSynthetic
    fun <V> property(
        behavior: WidgetProperty.Behavior = WidgetProperty.Behavior.PARTIAL_RENDER
    ): WidgetProperty<V?> = WidgetProperty<V?>(null, behavior).also(handledProperties::add)

    @JvmSynthetic
    fun <V> property(
        default: V,
        behavior: WidgetProperty.Behavior = WidgetProperty.Behavior.PARTIAL_RENDER
    ): WidgetProperty<V> = WidgetProperty(default, behavior).also(handledProperties::add)
}
