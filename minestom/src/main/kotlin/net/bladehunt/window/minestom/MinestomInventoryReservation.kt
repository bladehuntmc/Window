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

import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

class MinestomInventoryReservation(val inventory: Inventory) : Reservation<ItemStack> {
    override fun set(slot: Int2, pixel: ItemStack) {
        inventory[slot] = pixel
    }

    override fun get(slot: Int2): ItemStack = inventory[slot]
    override fun remove(slot: Int2) {
        inventory[slot] = ItemStack.AIR
    }

    override fun isEmpty(): Boolean = inventory.itemStacks.all { it.isAir }

    override fun isNotEmpty(): Boolean = inventory.itemStacks.any { !it.isAir }

    override fun clear() = inventory.clear()

    override fun iterator(): Iterator<Pair<Int2, ItemStack>> {
        val rowSize = inventory.inventoryType.rowSize
        return iterator {
            for (slot in 0..inventory.inventoryType.size) {
                val item = inventory[slot]
                if (item.isAir) continue
                yield(Int2(slot % rowSize, slot / rowSize) to item)
            }
        }
    }
}