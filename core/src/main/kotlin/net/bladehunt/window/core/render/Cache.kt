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

package net.bladehunt.window.core.render

import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.widget.Widget
import java.lang.ref.WeakReference

class Cache<T> {
    private val node: Node<T> = Node()

    fun search(path: List<Widget<T>>): Node<T>? {
        var node: Node<T> = node
        path.forEach { widget ->
            node = node.children.firstOrNull { it.widget?.get() == widget } ?: return null
        }
        return node
    }

    fun isCached(path: List<Widget<T>>): Boolean {
        var node: Node<T> = node
        path.forEach { widget ->
            node = node.children.firstOrNull { it.widget?.get() == widget } ?: return false
        }
        return node.layer != null
    }

    fun cache(path: List<Widget<T>>, layer: Layer<T>) {
        var node: Node<T> = node
        path.forEach { widget ->
            node = node.children.firstOrNull { it.widget?.get() == widget } ?: Node(node, WeakReference(widget)).also(node.children::add)
        }
        node.layer = layer
    }

    fun invalidate(widget: Widget<T>) {
        node.forEachDepthFirst { node ->
            if (node.widget?.get() == widget) {
                node.layer = null
            }
        }
    }

    fun invalidate(path: List<Widget<T>>) {
        var node: Node<T> = node
        path.forEachIndexed { index, widget ->
            node = node.children.firstOrNull { it.widget?.get() == widget } ?: return
            if (index == path.lastIndex) {
                node.layer = null
            }
        }
    }

    fun clear() {
        node.children.clear()
        node.layer = null
    }

    fun isEmpty(): Boolean {
        return node.children.isEmpty() && node.layer == null
    }

    fun size(): Int {
        var size = 0
        node.forEachDepthFirst { node ->
            if (node.layer != null) {
                size++
            }
        }
        return size
    }

    private fun clearWeakReferences() {
        node.forEachDepthFirst { node ->
            node.children.removeIf { child ->
                if (child.widget?.get() == null) {
                    child.layer = null
                    child.children.clear()
                    true
                } else {
                    false
                }
            }
        }
    }

    data class Node<T>(
        val parent: Node<T>? = null,
        val widget: WeakReference<Widget<T>>? = null,
        var layer: Layer<T>? = null,
        var size: Int2? = null,
        val children: MutableList<Node<T>> = arrayListOf()
    ) {
        fun forEachDepthFirst(visit: (Node<T>) -> Unit) {
            visit(this)
            children.forEach {
                it.forEachDepthFirst(visit)
            }
        }

        override fun toString(): String {
            return "Node(widget=$widget, layer=$layer, size=$size, children=$children)"
        }
    }
}