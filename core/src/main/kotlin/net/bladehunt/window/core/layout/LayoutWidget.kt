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
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.FlexedInts
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent

// needs better name
abstract class LayoutWidget<T>(override val size: FlexedInts) : Widget<T>(), WidgetParent<T> {

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

    private fun render(phase: Phase.RenderPhase<T>) {
        val layer = phase.layer

        // Finalize flexes
        val flexible = phase.node.children.filter { it.size.flexX }

        val (each, remainder) = calculateFlexes(flexible, layer, phase)

        renderWidget(phase.node.children, layer, phase, each, remainder)
    }

    override fun render(phase: Phase<T>) {
        when (phase) {
            is Phase.BuildPhase -> {
                val node = phase.node
                _children.forEach { widget ->
                    val childNode = node.searchLevel(widget) ?: node.createChild(widget, widget.size)
                    widget.render(phase.copy(node = childNode))
                }
                var sizeX = 0
                var flexX = false
                var sizeY = 0
                var flexY = false
                if (size.flexY) {
                    sizeY = 0
                    for (child in node.children) {
                        if (child.size.flexX) {
                            flexX = true
                            sizeX = size.x
                        }
                        if (!flexX) sizeX = child.size.x
                        if (child.size.flexY) {
                            flexY = true
                            sizeY = size.y
                        }
                        if (!flexY) sizeY += child.size.y
                    }
                }
                node.size = FlexedInts(
                    if (size.flexX) sizeX else size.x,
                    flexX && size.flexX,
                    if (size.flexY) sizeY else size.y,
                    flexY && size.flexY,
                )
            }
            is Phase.RenderPhase -> render(phase)
        }
    }

    private fun calculateFlexes(
        flexible: List<Window.Node<T>>,
        layer: Layer<T>,
        phase: Phase.RenderPhase<T>,
    ): Pair<Int, Int> {
        var each = 0
        var remainder = 0
        if (flexible.isNotEmpty()) {
            val availableSpace = layer.size.y - phase.node.children.sumOf { widget ->
                if (flexible.contains(widget)) 0 else widget.size.y
            }
            each = availableSpace.floorDiv(flexible.size)
            remainder = availableSpace % flexible.size
        }
        return Pair(each, remainder)
    }

    abstract fun renderWidget(
        children: MutableList<Window. Node<T>>,
        layer: Layer<T>,
        phase: Phase.RenderPhase<T>,
        each: Int,
        remainder: Int
    )

}
