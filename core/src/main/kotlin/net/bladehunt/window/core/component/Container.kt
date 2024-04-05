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

package net.bladehunt.window.core.component

import net.bladehunt.window.core.decoration.Background
import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

abstract class Container<Pixel>(
    override var size: Size2,
    val background: Background<Pixel>,
    val padding: Padding<Pixel>
) : Component<Pixel>, ParentComponent<Pixel> {
    private val children: MutableCollection<Component<Pixel>> = arrayListOf()

    override fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel) {
        reservation?.set(pos.copy(x = pos.x + padding.left, y = pos.y + padding.top), pixel)
    }

    override fun preRender(limits: Int2) {
        size = size.copy(
            x = if (size.flexX) limits.x else size.x,
            y = if (size.flexY) limits.y else size.y
        )
        val first = firstOrNull() ?: return
        val size = first.size
        first.preRender(Int2(
            if (size.flexX) this.size.x - padding.left - padding.right else size.x,
            if (size.flexY) this.size.y - padding.top - padding.bottom else size.y
        ))
    }
    override fun render() {
        val first = firstOrNull() ?: return
        first.render()
        val childPixels = first.reservation
            ?.associate { (pos, pixel) ->
                Int2(pos.x + padding.left, pos.y + padding.top) to pixel
            }
            ?: emptyMap()

        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val isPadding = x < padding.left || x >= size.x - padding.right ||
                        y < padding.top || y >= size.y - padding.bottom
                val pos = Int2(x, y)
                val pixel = if (isPadding) {
                    padding.pixel
                } else childPixels[pos] ?: background.item ?: continue
                reservation?.set(pos, pixel)
            }
        }
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Component<Pixel>> = children.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Component<Pixel>): Boolean = children.add(child)
}