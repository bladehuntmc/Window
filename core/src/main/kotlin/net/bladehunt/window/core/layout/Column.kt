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

open class Column<T>(override val size: FlexPair) : LayoutWidget<T>(size){
    override fun renderWidget(
        children: MutableList<Window.Node<T>>,
        layer: Layer<T>,
        phase: Phase.RenderPhase<T>,
        each: Int,
        remainder: Int
    ) {
        var pos = 0
        children.forEachIndexed { index, node ->
            val widget = node.widget ?: return
            widget.setUpdateHandler(this) {
                requestUpdate()
            }

            val offsetLayer = OffsetLimitedLayer(
                layer,
                0,
                pos,
                IntPair(
                    if (node.size.flexX) layer.size.x else node.size.x,
                    if (node.size.flexY) each + (if (index < remainder) 1 else 0) else node.size.y
                )
            )
            widget.render(
                phase.copy(node = node, layer = offsetLayer)
            )

            pos += offsetLayer.size.y
        }
    }

    override fun build(node: Window.Node<T>) : FlexValues {
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

        return(FlexValues(sizeX, flexX, sizeY, flexY))
    }
}