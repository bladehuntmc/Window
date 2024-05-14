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
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.FlexPair
import kotlin.math.max

class Auto<T>(size: FlexPair) : LayoutWidget<T>(size) {
    override fun calculateSize(node: Window.Node<T>): FlexPair = size

    override fun onRender(phase: Phase.RenderPhase<T>) {
        val layer = phase.layer

        var pointerX = 0
        var pointerY = 0
        var rowHeight = 0
        phase.node.children.forEach { child ->
            val widget = child.widget ?: return@forEach

            if (pointerX + child.size.x > layer.size.x) {
                pointerX = 0
                pointerY += rowHeight
                rowHeight = 0
            }

            widget.setUpdateHandler(this) {
                requestUpdate()
            }

            val offsetLayer = OffsetLimitedLayer(
                layer,
                pointerX,
                pointerY,
                IntPair(
                    if (child.size.flexX) 1 else child.size.x,
                    if (child.size.flexY) 1 else child.size.y
                )
            )
            pointerX += child.size.x
            rowHeight = max(rowHeight, child.size.y)

            widget.render(phase.copy(node = child, layer = offsetLayer))
        }
    }
}