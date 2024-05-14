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
import net.bladehunt.window.core.layer.OffsetLimitedLayer
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.FlexPair

open class Row<T>(override val size: FlexPair) : LayoutWidget<T>(size) {
    override fun onRender(phase: Phase.RenderPhase<T>) {
        val (_, _, node, layer) = phase
        val flexible = children.filter { it.size.flexX }
        var each = 0
        var remainder = 0
        if (flexible.isNotEmpty()) {
            val availableSpace = layer.size.x - children.sumOf { widget ->
                if (flexible.contains(widget)) 0 else widget.size.x
            }
            each = availableSpace.floorDiv(flexible.size)
            remainder = availableSpace % flexible.size
        }

        var pos = 0
        node.children.forEachIndexed { index, child ->
            val widget = child.widget ?: return@forEachIndexed
            widget.setUpdateHandler(this) {
                requestUpdate()
            }

            val offsetLayer = OffsetLimitedLayer(
                layer,
                0,
                pos,
                IntPair(
                    if (child.size.flexX) each + (if (index >= remainder) 0 else 1) else child.size.x,
                    if (child.size.flexY) layer.size.y else child.size.y
                )
            )
            widget.render(phase.copy(node = child, layer = offsetLayer))

            pos += offsetLayer.size.x
        }
    }

    override fun calculateSize(node: Window.Node<T>) : FlexPair {
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
                if (!flexX) sizeX += child.size.x
                if (child.size.flexY) {
                    flexY = true
                    sizeY = size.y
                }
                if (!flexY) sizeY = child.size.y
            }
        }

        return(FlexPair(sizeX, flexX, sizeY, flexY))
    }
}