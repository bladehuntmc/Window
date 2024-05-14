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

import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.Phase
import net.bladehunt.window.core.util.FlexedInts

class Switch<T> : WidgetParent<T>, Widget<T>() {
    override val size: FlexedInts =
        FlexedInts()
    var index by Signal(0)

    private val _children = arrayListOf<Widget<T>>()
    override val children: Collection<Widget<T>>
        get() = _children.toList()

    override fun <W : Widget<T>> addWidget(widget: W, index: Int?) {
        _children.add(widget)
    }

    override fun <W : Widget<T>> removeWidget(widget: W) {
        _children.remove(widget)
    }

    override fun render(phase: Phase<T>) {
        when (phase) {
            is Phase.BuildPhase -> {
                phase.node.children.firstOrNull()?.widget?.removeUpdateHandler(this)
                phase.node.children.clear()
                val widget = _children.getOrNull(index) ?: return
                val node = phase.node.createChild(widget, widget.size)
                widget.render(phase.copy(node = node))
                widget.setUpdateHandler(this, this::requestUpdate)
                phase.node.size = node.size
            }
            is Phase.RenderPhase -> {
                val node = phase.node.children.firstOrNull() ?: return
                node.widget?.render(phase.copy(node = node))
            }
        }
    }
}