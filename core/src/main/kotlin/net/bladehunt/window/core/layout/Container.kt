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
import net.bladehunt.window.core.layer.OffsetLimitedLayer
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent

class Container<T>(override val size: Size2) : WidgetParent<T>, Widget<T>() {
    data class Padding<T>(val left: Int, val right: Int, val top: Int, val bottom: Int, val item: T?)
    var padding: Padding<T> = Padding(0, 0, 0, 0, null)

    private var child: Widget<T>? = null
    override val children: Collection<Widget<T>>
        get() = listOfNotNull(child)

    override fun <W : Widget<T>> addWidget(widget: W, index: Int?) {
        child = widget
    }

    override fun <W : Widget<T>> removeWidget(widget: W) {
        if (child == widget) child = null
    }

    override fun render(phase: Phase<T>) {
        when (phase) {
            is Phase.BuildPhase -> {
                val widget = child ?: return
                val node = phase.node.searchLevel(widget) ?: phase.node.createChild(
                    widget,
                    widget.size
                )
                child?.render(phase.copy(
                    node = node
                ))
                if (size.flexX || size.flexY) {
                    phase.node.size = Size2(
                        node.size.x + padding.left + padding.right,
                        node.size.flexX,
                        node.size.y + padding.top + padding.bottom,
                        node.size.flexY,
                    )
                }
            }
            is Phase.RenderPhase -> {
                val node = phase.node.children.firstOrNull() ?: return
                for (x in 0..<phase.layer.size.x) {
                    for (y in 0..<phase.layer.size.y) {
                        if (y < padding.top || y >= phase.layer.size.y - padding.bottom || x < padding.left || x >= phase.layer.size.x - padding.right) {
                            val padWith = padding.item ?: break
                            phase.layer[x, y] = padWith
                        }
                    }
                }
                val widget = node.widget ?: return
                widget.setUpdateHandler(this) {
                    requestUpdate()
                }
                widget.render(phase.copy(
                    layer = OffsetLimitedLayer(
                        phase.layer, padding.left, padding.top,
                        Int2(phase.layer.size.x - padding.left - padding.right, phase.layer.size.y - padding.top - padding.bottom)
                    ),
                    node = node
                ))
            }
        }
    }
}