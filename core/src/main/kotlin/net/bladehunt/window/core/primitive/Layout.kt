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

abstract class Layout<T> : Primitive<T>(), PrimitiveParent<T> {
    private val _children: MutableList<Primitive<T>> = arrayListOf()
    override val children: Collection<Primitive<T>>
        get() = _children.toList()

    override fun <W : Primitive<T>> removeWidget(widget: W) {
        _children.remove(widget)
    }

    override fun <W : Primitive<T>> addPrimitive(widget: W, index: Int?) {
        if (_children.contains(widget)) return
        if (index == null) {
            _children.add(widget)
        } else _children.add(index, widget)
    }

    abstract fun calculateSize(node: Window.Node<T>, context: Context): Size

    override fun build(parent: Window.Node<T>) {
        parent.createChild(this) {
            this@Layout._children.forEach { it.build(this) }
            size = calculateSize(this, context)
        }
    }
}
