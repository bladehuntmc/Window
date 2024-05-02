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

package net.bladehunt.window.core.widget

import net.bladehunt.window.core.exception.WindowOverflowException
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.reservation.Resizable
import net.bladehunt.window.core.util.Int2

abstract class Column<Pixel>(
    override val reservation: Reservation<Pixel>
) : Widget<Pixel>, ParentWidget<Pixel> {
    private val children: MutableCollection<Widget<Pixel>> = arrayListOf()
    protected val offsets: MutableMap<Reservation<Pixel>, Int> = hashMapOf()

    override fun updateOne(reservation: Reservation<Pixel>, posX: Int, posY: Int, pixel: Pixel) {
        val offset = offsets[reservation] ?: return
        this.reservation[posX, posY + offset] = pixel
    }

    override fun removeOne(reservation: Reservation<Pixel>, posX: Int, posY: Int,) {
        val offset = offsets[reservation] ?: return
        this.reservation.remove(posX, posY + offset)
    }

    override fun preRender(limits: Int2): Int2 {
        var totalX = 0
        var totalY = 0

        val flexSpace = limits.y - sumOf { if (!it.size.flexY) it.size.y else 0 }
        val flexItems = filter { it.size.flexY }

        if (flexItems.size > flexSpace) throw WindowOverflowException("There were too many components when trying to render the column")

        val each = if (flexItems.isNotEmpty()) flexSpace.floorDiv(flexItems.size) else 0
        var remainder = if (flexItems.isNotEmpty()) flexSpace % flexItems.size else 0

        forEach { component ->
            val size = component.size
            val sizeY = if (flexItems.contains(component)) {
                val sizeY = each + remainder.coerceIn(0, 1)
                remainder--
                sizeY
            } else size.y

            offsets[component.reservation] = totalY

            val usedSpace = component.preRender(Int2(limits.x, sizeY))
            (component.reservation as? Resizable)?.resize(usedSpace.x, usedSpace.y)

            if (usedSpace.x > totalX) totalX = usedSpace.x
            totalY += usedSpace.y
        }

        return Int2(if (size.flexX) totalX else size.x, if (size.flexY) totalY else size.y)
    }

    override fun render() {
        forEach { component ->
            component.render()
        }
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Widget<Pixel>> = children.iterator()
    override fun removeChild(child: Widget<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Widget<Pixel>): Boolean = children.add(child)
}