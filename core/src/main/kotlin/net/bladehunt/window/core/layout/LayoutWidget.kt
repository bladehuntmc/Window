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

import net.bladehunt.window.core.Phase
import net.bladehunt.window.core.util.FlexPair
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent

abstract class LayoutWidget<T>(override val size: FlexPair) : Widget<T>(), WidgetParent<T> {
    private val _children: MutableList<Widget<T>> = arrayListOf()
    override val children: Collection<Widget<T>>
        get() = _children.toList()

    override fun <W : Widget<T>> removeWidget(widget: W) {
        _children.remove(widget)
    }

    override fun <W : Widget<T>> addWidget(widget: W, index: Int?) {
        if (_children.contains(widget)) return
        if (index == null) {
            _children.add(widget)
        } else _children.add(index, widget)
    }

    override fun render(phase: Phase<T>) {
        when (phase) {
            is Phase.BuildPhase -> onBuild(phase)
            is Phase.RenderPhase -> onRender(phase)
        }
    }

    abstract fun calculateSize(node: Window.Node<T>): FlexPair

    abstract fun onRender(phase: Phase.RenderPhase<T>)

    open fun onBuild(phase: Phase.BuildPhase<T>) {
        val node = phase.node
        _children.forEach { widget ->
            val childNode = node.searchLevel(widget) ?: node.createChild(widget, widget.size)
            widget.render(phase.copy(node = childNode))
        }
        node.size = calculateSize(node)
    }
}
