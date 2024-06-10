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

import net.bladehunt.window.core.Context
import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.core.widget.Layout

abstract class AbstractContainer<T>(size: Size) : Layout<T>(size) {
    abstract fun createPadding(): Padding<T>

    override fun calculateSize(node: Window.Node<T>, context: Context): Size {
        val padding = createPadding()
        val sizeX = padding.left + padding.right
        val sizeY = padding.top + padding.bottom

        val child =
            node.children.firstOrNull()
                ?: return Size(sizeX, sizeY, size.flexBasisX, size.flexBasisY)

        return Size(
            sizeX + child.size.x,
            sizeY + child.size.y,
            if (child.size.flexBasisX == 0) 0 else size.flexBasisX,
            if (child.size.flexBasisY == 0) 0 else size.flexBasisY
        )
    }

    override fun render(node: Window.Node<T>) {
        val layer = requireNotNull(node.layer)

        val padding = createPadding()

        for (x in 0..<layer.size.x) {
            for (y in 0..<layer.size.y) {
                if (
                    y < padding.top ||
                        y >= layer.size.y - padding.bottom ||
                        x < padding.left ||
                        x >= layer.size.x - padding.right
                ) {
                    val padWith = padding.item ?: break
                    layer[x, y] = padWith
                }
            }
        }
        val child = node.children.firstOrNull() ?: return
        child.widget.render(child)
    }
}
