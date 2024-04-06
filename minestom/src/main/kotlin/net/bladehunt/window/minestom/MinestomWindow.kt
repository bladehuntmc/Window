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

import net.bladehunt.reakt.reactivity.Effect
import net.bladehunt.reakt.reactivity.Memo
import net.bladehunt.reakt.reactivity.MemoResult
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Column
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

class MinestomWindow(
    reservation: MinestomInventoryReservation
) : InteractionHandler<MinestomInteraction>, Column<ItemStack>(
    Size2(
        reservation.inventory.inventoryType.rowSize,
        reservation.inventory.inventoryType.size / reservation.inventory.inventoryType.rowSize
    )
) {
    init {
        reservation.inventory.addInventoryCondition { player, slot, clickType, inventoryConditionResult ->
            val pos = Int2(slot % reservation.inventory.inventoryType.rowSize, slot / reservation.inventory.inventoryType.rowSize)
            onEvent(
                MinestomInteraction.InventoryCondition(
                    player,
                    pos,
                    pos,
                    clickType,
                    inventoryConditionResult
                )
            )
        }
    }
    override var reservation: Reservation<ItemStack>? = reservation

    val inventory: Inventory
        get() = (reservation as MinestomInventoryReservation).inventory
    var title: net.kyori.adventure.text.Component
        get() = (reservation as MinestomInventoryReservation).inventory.title
        set(value) {
            (reservation as MinestomInventoryReservation).inventory.title = value
        }

    @WindowDsl
    fun title(block: @WindowDsl Memo<net.kyori.adventure.text.Component>.(MemoResult) -> net.kyori.adventure.text.Component) {
        val memo = Memo(block)
        Effect {
            val memoValue by memo
            title = memoValue
        }
    }

    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                val iter = offsets.toList().sortedBy { it.second }.iterator()
                val first = iter.next()

                var offset = first.second
                var component: Component<ItemStack> = first.first

                while (iter.hasNext()) {
                    val (nextComponent, nextOffset) = iter.next()
                    if (event.clickPos.y in offset..<nextOffset) {
                        break
                    }
                    component = nextComponent
                    offset = nextOffset
                }

                val childEvent = event.copy(
                    clickPos = event.clickPos.copy(y = event.clickPos.y - offset)
                )
                if (childEvent.clickPos.y >= component.size.y  || childEvent.clickPos.x >= component.size.x) return
                try {
                    @Suppress("UNCHECKED_CAST")
                    (component as? InteractionHandler<MinestomInteraction>)?.onEvent(childEvent)
                } catch (_: ClassCastException) {}
            }
        }
    }
}