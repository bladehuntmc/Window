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

package net.bladehunt.window.minestom

import net.bladehunt.kotstom.util.EventNodeContainerInventory
import net.bladehunt.window.core.reservation.ArrayReservationImpl
import net.bladehunt.window.core.reservation.HookReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.reservation.Resizable
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Widget
import net.bladehunt.window.core.widget.WidgetParent
import net.kyori.adventure.text.Component
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

class MinestomWindow(
    inventoryType: InventoryType,
    title: Component = Component.text("Window"),
) : WidgetParent<WindowItem>, Widget<WindowItem> {
    val inventory = EventNodeContainerInventory(inventoryType, title)

    private val offsets = hashMapOf<Reservation<WindowItem>, Int>()
    private val _widgets = mutableListOf<Widget<WindowItem>>()

    override val reservation: Reservation<WindowItem> = MinestomInventoryReservation(inventory)

    override val widgets: Collection<Widget<WindowItem>>
        get() = _widgets.toList()

    override fun createReservation(size: Size2): Reservation<WindowItem> {
        return HookReservation(ArrayReservationImpl(size), this::onSet, this::onRemove)
    }

    override fun addWidget(widget: Widget<WindowItem>) {
        _widgets.add(widget)
    }

    override fun calculateFlex() {
        val flexWidgets = _widgets.filter { it.reservation.size.flexY && it.reservation is Resizable }
        flexWidgets.forEach { widget ->
            (widget.reservation as Resizable).resize(
                widget.reservation.size.x,
                flexWidgets.size / this.reservation.size.y
            )
        }
    }

    override fun render() {
        calculateFlex()

        var previousPosY = 0
        _widgets.forEach {
            offsets[it.reservation] = previousPosY
            previousPosY += it.reservation.size.y
            it.render()
        }
    }

    private fun onSet(reservation: Reservation<WindowItem>, posX: Int, posY: Int, item: WindowItem) {
        val offset = offsets[reservation] ?: return
        this.reservation[posX, posY + offset] = item
    }
    private fun onRemove(reservation: Reservation<WindowItem>, posX: Int, posY: Int) {
        val offset = offsets[reservation] ?: return
        this.reservation[posX, posY + offset] = ItemStack.AIR to null
    }
}