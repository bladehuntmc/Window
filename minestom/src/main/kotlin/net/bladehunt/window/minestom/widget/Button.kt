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

import net.bladehunt.window.core.interaction.Interactable
import net.bladehunt.window.core.interaction.Interaction
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.minestom.event.MinestomEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class Button(
    override val reservation: Reservation<ItemStack>,
    override val interactionReservation: Reservation<Interaction<MinestomEvent>>
) : Widget<ItemStack>, Interactable<MinestomEvent> {
    override fun preRender(limits: Int2): Int2 {
        return Int2(1, 1)
    }

    override fun render() {
        reservation[0, 0] = ItemStack.of(Material.STONE)
        println("rendered button")
        reservation.iterator().forEach(::println)
    }

    override fun onInteract(event: MinestomEvent) {
    }
}