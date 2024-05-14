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
import net.bladehunt.window.core.util.FlexPair
import net.bladehunt.window.core.util.IntPair

class Container<T>(size: FlexPair) : LayoutWidget<T>(size) {
    data class Padding<T>(val left: Int, val right: Int, val top: Int, val bottom: Int, val item: T?)
    var padding: Padding<T> = Padding(0, 0, 0, 0, null)

    override fun calculateSize(node: Window.Node<T>): FlexPair {
        val widget = node.widget
        if (widget != null && (size.flexX || size.flexY)) {
            return FlexPair(
                if (size.flexX)
                        (if (widget.size.flexX) 0 else widget.size.x + padding.left + padding.right)
                else size.x,
                size.flexX && widget.size.flexX,
                if (size.flexY)
                    (if (widget.size.flexY) 0 else widget.size.y + padding.left + padding.right)
                else size.y,
                size.flexY && widget.size.flexY,
            )
        }
        return size
    }

    override fun onRender(phase: Phase.RenderPhase<T>) {
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
                IntPair(phase.layer.size.x - padding.left - padding.right, phase.layer.size.y - padding.top - padding.bottom)
            ),
            node = node
        ))
    }
}