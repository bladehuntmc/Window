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

import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.layer.OffsetLimitedLayer
import net.bladehunt.window.core.render.RenderContext
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent

open class Row<T>(override val size: Size2) : Widget<T>(), WidgetParent<T> {
    private val _children: MutableList<Widget<T>> = arrayListOf()
    override val children: Collection<Widget<T>>
        get() = _children.toList()

    override fun <W : Widget<T>> removeWidget(widget: W) {
        _children.remove(widget)
    }

    override fun <W : Widget<T>> addWidget(widget: W, index: Int?) {
        if (index == null) {
            _children.add(widget)
        } else _children.add(index, widget)
    }

    override fun render(layer: Layer<T>, context: RenderContext<T>): Int2 {
        // Calculate flexes
        val flexible = _children.filter { it.size.flexX }
        var each = 0
        var remainder = 0
        if (flexible.isNotEmpty()) {
            val availableSpace = layer.size.x - _children.sumOf { widget ->
                if (flexible.contains(widget)) 0 else widget.size.x
            }
            each = availableSpace.floorDiv(flexible.size)
            remainder = availableSpace % flexible.size
        }

        // Render layers if needed
        var previousPosX = 0
        var maxSizeY = 0
        _children.forEachIndexed { index, widget ->
            widget.setUpdateHandler(this) {
                requestUpdate()
            }

            val final = widget.render(
                OffsetLimitedLayer(
                    layer,
                    previousPosX,
                    0,
                    Int2(
                        if (widget.size.flexX) each + (if (index < remainder) 1 else 0) else widget.size.x,
                        layer.size.y
                    )
                ),
                context.copy(path = listOf(*context.path.toTypedArray(), widget))
            )

            if (final.y > maxSizeY) maxSizeY = final.y
            previousPosX += final.x
        }

        return Int2(previousPosX, maxSizeY)
    }
}