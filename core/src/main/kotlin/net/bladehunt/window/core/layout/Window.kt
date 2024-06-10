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

package net.bladehunt.window.core.layout

import net.bladehunt.window.core.Context
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.widget.Widget

abstract class Window<T>(size: Size) : AbstractColumn<T>(size) {
    abstract val parentNode: Node<T>

    fun buildNode(context: Context) {
        buildNode(parentNode, context)
    }

    abstract fun render()

    override fun buildNode(parent: Node<T>, context: Context) {
        children.forEach { it.buildNode(parentNode, context) }
    }

    abstract fun createArrayLayer(sizeX: Int, sizeY: Int): ArrayLayerImpl<T>

    data class Node<T>(
        val parent: Node<T>? = null,
        val widget: Widget<T>,
        var context: Context,
        var size: Size = Size(0, 0),
        val children: MutableList<Node<T>> = arrayListOf(),
        var layer: Layer<T>? = null,
    ) {
        fun addChild(node: Node<T>) {
            children.add(node)
        }

        fun createChild(widget: Widget<T>, context: Context): Node<T> =
            Node(this, widget, context, widget.size).apply(::addChild)

        fun removeChild(node: Node<T>) = children.remove(node)

        fun removeChild(widget: Widget<T>) = children.removeIf { it == widget }

        fun searchLevel(widget: Widget<T>): Node<T>? = children.firstOrNull { it.widget == widget }

        fun hasChild(widget: Widget<T>): Boolean = children.any { it.widget == widget }

        fun traverseDepthFirst(visit: (Node<T>) -> Unit) {
            visit(this)
            children.forEach { it.traverseDepthFirst(visit) }
        }

        fun getRoot(): Node<T> {
            var current = this
            while (current.parent != null) {
                current = current.parent!!
            }
            return current
        }

        override fun toString(): String {
            return "Node(widget=$widget, size=$size, children=$children)"
        }
    }
}
