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

import net.bladehunt.kotstom.dsl.listenOnly
import net.bladehunt.kotstom.util.EventNodeContainerInventory
import net.bladehunt.window.core.interaction.Interactable
import net.bladehunt.window.core.interaction.Interaction
import net.bladehunt.window.core.widget.Column
import net.bladehunt.window.core.reservation.HookReservation
import net.bladehunt.window.core.reservation.ArrayReservationImpl
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.reservation.ResizableHookReservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.event.MinestomEvent
import net.kyori.adventure.text.Component
import net.minestom.server.Viewable
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.inventory.click.Click
import net.minestom.server.item.ItemStack

class MinestomWindow(
    val inventory: EventNodeContainerInventory
) : Column<ItemStack>(
    MinestomInventoryReservation(inventory)
), Interactable<MinestomEvent>, Viewable {
    constructor(
        inventoryType: InventoryType,
        title: Component = Component.text("Window")
    ) : this(EventNodeContainerInventory(inventoryType, title))
    override fun createReservation(size: Size2): HookReservation<ItemStack> {
        return ResizableHookReservation(ArrayReservationImpl<ItemStack>(size), this::updateOne, this::removeOne)
    }
    init {
        inventory.eventNode().listenOnly<InventoryPreClickEvent> {
            when (val clickInfo = it.clickInfo) {
                is Click.Info.Left -> {
                    onInteract(MinestomEvent.PreClickEvent(clickInfo.slot, inventory.getItemStack(clickInfo.slot)))
                }
                else -> {}
            }
        }
    }

    override val interactionReservation: Reservation<Interaction<MinestomEvent>> = MinestomInventoryInteractionReservation(inventory)

    override fun onInteract(event: MinestomEvent) {
        when (event) {
            is MinestomEvent.PreClickEvent -> {
                println("you clicked ${event.slot}")
            }
        }
    }

    override fun addViewer(player: Player): Boolean {
        player.openInventory(inventory)
        return true
    }

    override fun removeViewer(player: Player): Boolean {
        if (player.openInventory != inventory) return false
        player.closeInventory()
        return true
    }

    override fun getViewers(): MutableSet<Player> = inventory.viewers
}