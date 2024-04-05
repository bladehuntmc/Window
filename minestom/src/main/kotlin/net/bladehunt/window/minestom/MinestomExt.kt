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

@file:JvmName("MinestomExt")

package net.bladehunt.window.minestom

import net.bladehunt.window.core.util.Int2
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

val InventoryType.rowSize: Int
    get() = when (this) {
        InventoryType.CHEST_1_ROW,
        InventoryType.CHEST_2_ROW,
        InventoryType.CHEST_3_ROW,
        InventoryType.CHEST_4_ROW,
        InventoryType.CHEST_5_ROW,
        InventoryType.CHEST_6_ROW,
        InventoryType.SHULKER_BOX -> 9

        InventoryType.WINDOW_3X3,
        InventoryType.CRAFTER_3X3,
        InventoryType.CRAFTING,
        InventoryType.ANVIL -> 3

        InventoryType.HOPPER -> 7
        
        else -> 1
    }

operator fun Inventory.get(slot: Int): ItemStack = this.getItemStack(slot)
operator fun Inventory.set(slot: Int, itemStack: ItemStack) {
    this.setItemStack(slot, itemStack)
}

operator fun Inventory.get(slot: Int2): ItemStack = this.getItemStack(slot.y * inventoryType.rowSize + slot.x)
operator fun Inventory.set(slot: Int2, itemStack: ItemStack) {
    this.setItemStack(slot.y * inventoryType.rowSize + slot.x, itemStack)
}

operator fun Inventory.get(x: Int, y: Int): ItemStack = this.getItemStack(y * inventoryType.rowSize + x)
operator fun Inventory.set(x: Int, y: Int, itemStack: ItemStack) {
    this.setItemStack(y * inventoryType.rowSize + x, itemStack)
}