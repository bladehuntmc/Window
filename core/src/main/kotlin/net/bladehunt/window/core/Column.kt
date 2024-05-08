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

package net.bladehunt.window.core

import net.bladehunt.window.core.exception.WindowOverflowException
import net.bladehunt.window.core.reservation.OffsetLimitedReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent

abstract class Column<T> : Widget<T>(), WidgetParent<T> {
    override var isDirty: Boolean
        get() = _children.any { it.isDirty }
        set(_) {}

    private val _children: MutableList<Widget<T>> = arrayListOf()
    override val children: Collection<Widget<T>>
        get() = _children.toList()

    override fun <W : Widget<T>> addWidget(widget: W) {
        _children.add(widget)
    }

    override fun onRender(reservation: Reservation<T>): Int2 {
        val flexible = _children.filter { it.size.flexY }
        var each = 0
        var remainder = 0
        if (flexible.isNotEmpty()) {
            val availableSpace = reservation.size.y - _children.sumOf { widget ->
                if (flexible.contains(widget)) 0 else widget.size.y
            }
            each = availableSpace.floorDiv(flexible.size)
            remainder = availableSpace % flexible.size
            if (each == 0) throw WindowOverflowException("There wasn't enough space to fit the flexible components.")
        }

        var previousPosY = 0
        var maxSizeX = 0
        _children.forEachIndexed { index, widget ->
            val offsetY = previousPosY
            val res = OffsetLimitedReservation(
                reservation,
                0,
                offsetY,
                Int2(
                    reservation.size.x,
                    if (widget.size.flexX) each + (if (index < remainder) 1 else 0) else widget.size.y
                )
            )

            widget.addUpdateHandler(this) {
                requestUpdate()
            }

            val final = widget.render(res)
            if (final.x > maxSizeX) maxSizeX = final.x
            previousPosY += final.y
        }
        return Int2(maxSizeX, previousPosY)
    }
}