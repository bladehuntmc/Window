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

package net.bladehunt.window.core.primitive

import kotlin.math.max
import kotlin.math.roundToInt
import net.bladehunt.window.core.Context
import net.bladehunt.window.core.layer.OffsetLimitedLayer
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.widget.Layout

abstract class AbstractColumn<T>(size: Size = Size(0, 0)) : Layout<T>(size) {
    override fun calculateSize(node: Window.Node<T>, context: Context): Size {
        val (sizeX, sizeY, flexX, flexY) = size

        if (flexX == 0 && flexY == 0) return Size(sizeX, sizeY)

        var newMinX = 0
        var newMinY = 0
        var requiresFlexX = false
        var requiresFlexY = false
        for (child in node.children) {
            val childSize = child.size
            requiresFlexX = requiresFlexX || childSize.flexBasisX > 0
            requiresFlexY = requiresFlexY || childSize.flexBasisY > 0

            newMinX += max(newMinX, childSize.x)
            newMinY += childSize.y
        }

        return Size(
            max(newMinX, sizeX),
            max(newMinY, sizeY),
            if (requiresFlexX) flexX else 0,
            if (requiresFlexX) flexX else 0
        )
    }

    override fun render(node: Window.Node<T>) {
        val layer = requireNotNull(node.layer)

        val totalFlexSpace = layer.size.y - node.children.sumOf { child -> child.size.y }
        val totalFlexYBasis = node.children.sumOf { child -> child.size.flexBasisY }
        var previousY = 0
        node.children.forEach { child ->
            val childLayer =
                OffsetLimitedLayer(
                    layer,
                    0,
                    previousY,
                    IntPair(
                        if (child.size.flexBasisX > 0) layer.size.x else child.size.x,
                        if (child.size.flexBasisY == 0) child.size.y
                        else
                            child.size.y +
                                (totalFlexSpace *
                                        child.size.flexBasisY.toDouble().div(totalFlexYBasis))
                                    .roundToInt()
                    )
                )
            child.layer = childLayer
            child.widget.render(child)

            previousY += childLayer.size.y
        }
    }
}
