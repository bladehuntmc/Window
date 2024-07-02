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

package net.bladehunt.window_old.core.primitive

import kotlin.math.max
import net.bladehunt.window.core.Context
import net.bladehunt.window.core.layer.OffsetLimitedLayer
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.Size

abstract class AbstractAuto<T> : Layout<T>() {
    override fun calculateSize(node: Window.Node<T>, context: Context): Size = size

    override fun render(node: Window.Node<T>) {
        val layer = node.layer ?: throw IllegalStateException("The layer must not be null!")

        var pointerX = 0
        var pointerY = 0
        var rowHeight = 0
        node.children.forEach { child ->
            val widget = child.primitive

            if (pointerX + child.size.x > layer.size.x) {
                pointerX = 0
                pointerY += rowHeight
                rowHeight = 0
            }

            node.layer =
                OffsetLimitedLayer(
                    layer,
                    pointerX,
                    pointerY,
                    IntPair(
                        if (child.size.flexBasisX > 0) 1 else child.size.x,
                        if (child.size.flexBasisY > 0) 1 else child.size.y))
            pointerX += child.size.x
            rowHeight = max(rowHeight, child.size.y)

            widget.render(child)
        }
    }
}
