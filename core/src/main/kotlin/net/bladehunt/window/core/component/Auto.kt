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

import net.bladehunt.window.core.WindowOverflowException
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

abstract class Auto<Pixel>(override var size: Size2) : Component<Pixel>, ParentComponent<Pixel> {
    private val children: MutableCollection<Component<Pixel>> = arrayListOf()
    protected val offsets: MutableMap<Component<Pixel>, Int2> = hashMapOf()

    override fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel) {
        val offset = offsets[component] ?: return
        reservation?.set(pos + offset, pixel)
    }

    override fun preRender(limits: Int2) {
        var totalX = 0
        var totalY = 0

        var pointerX = 0
        var rowHeight = 0
        forEach { component ->
            if (pointerX + component.size.x > limits.x) {
                if (pointerX > totalX) totalX = pointerX
                totalY += rowHeight
                pointerX = 0
                rowHeight = 0
            }
            if (component.size.x > limits.x) throw WindowOverflowException("Component size X didn't fit within limits!")
            if (component.size.y > limits.y + totalY) throw WindowOverflowException("Component size Y didn't fit within limits!")

            offsets[component] = Int2(pointerX, totalY)

            pointerX += component.size.x
            if (component.size.y > rowHeight) rowHeight = component.size.y

            component.preRender(Int2(
                x = if (component.size.flexX) 1 else component.size.x,
                y = if (component.size.flexY) 1 else component.size.y,
            ))
        }

        size = size.copy(
            x = if (size.flexX) totalX else size.x,
            y = if (size.flexY) totalY else size.y
        )
    }

    override fun render() {
        forEach { component ->
            component.render()
        }
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Component<Pixel>> = children.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Component<Pixel>): Boolean = children.add(child)
}