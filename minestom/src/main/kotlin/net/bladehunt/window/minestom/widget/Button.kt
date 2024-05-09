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

package net.bladehunt.window.minestom.widget

import net.bladehunt.window.core.interact.Interaction
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.minestom.WindowItem
import net.bladehunt.window.minestom.event.MinestomEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class Button(
    itemStack: ItemStack = ItemStack.of(Material.STONE),
    interaction: Interaction<MinestomEvent> = Interaction { _ -> },
) : Widget<WindowItem>() {
    override val size: Size2 = Size2(1, true, 1, true)
    var interaction: Button.() -> Interaction<MinestomEvent> = { interaction }
    var itemStack: Button.() -> ItemStack = { itemStack }

    var finalSize: Button.() -> Int2 = { size.toInt2() }

    override fun onRender(reservation: Reservation<WindowItem>): Int2 {
        val size = finalSize()
        for (x in 0..<size.x) {
            for (y in 0..<size.y) {
                reservation[x, y] = itemStack() to interaction()
            }
        }
        return size
    }
}