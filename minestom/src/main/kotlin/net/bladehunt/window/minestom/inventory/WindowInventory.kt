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

package net.bladehunt.window.minestom.inventory

import net.bladehunt.kotstom.util.EventNodeContainerInventory
import net.kyori.adventure.text.Component
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.inventory.InventoryItemChangeEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

class WindowInventory(
    inventoryType: InventoryType,
    title: Component = Component.text("Window")
) : EventNodeContainerInventory(inventoryType, title) {
    constructor(inventoryType: InventoryType, title: String) : this(inventoryType, Component.text(title))

    fun transaction(block: (Transaction) -> Unit) {
        lock.lock()
        val previous = itemStacks.copyOf()
        try { block(Transaction()) } catch (_: IllegalArgumentException) {}
        itemStacks.forEachIndexed { index, itemStack ->
            if (previous[index] != itemStack) updateSlot(index, itemStack)
            EventDispatcher.call(
                InventoryItemChangeEvent(this, index, previous[index], itemStack)
            )
        }
        lock.unlock()
    }

    inner class Transaction {
        val itemStacks: Array<ItemStack> get() = this@WindowInventory.itemStacks

        val size: Int get() = this@WindowInventory.size

        fun isEmpty(): Boolean = itemStacks.all { it.isAir }

        fun isNotEmpty(): Boolean = itemStacks.any { !it.isAir }

        fun clear() {
            for (i in itemStacks.indices) {
                UNSAFE_itemInsert(i, ItemStack.AIR)
            }
        }

        operator fun get(slot: Int): ItemStack = itemStacks[slot]

        operator fun set(slot: Int, itemStack: ItemStack) {
            UNSAFE_itemInsert(slot, itemStack)
        }
    }
}