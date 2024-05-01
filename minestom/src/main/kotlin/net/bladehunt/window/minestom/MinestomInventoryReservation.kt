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

import net.bladehunt.kotstom.extension.get
import net.bladehunt.kotstom.extension.rowSize
import net.bladehunt.kotstom.extension.set
import net.bladehunt.kotstom.util.EventNodeContainerInventory
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.minestom.server.item.ItemStack

class MinestomInventoryReservation(
    private val inventory: EventNodeContainerInventory
) : Reservation<ItemStack> {
    override val size: Size2 = Size2(inventory.inventoryType.rowSize, inventory.size / inventory.inventoryType.rowSize)
    override val absoluteSize: Int2 = Int2(size.x, size.y)

    override fun set(posX: Int, posY: Int, pixel: ItemStack) {
        inventory[posX, posY] = pixel
    }

    override fun get(posX: Int, posY: Int): ItemStack = inventory[posX, posY]

    override fun remove(posX: Int, posY: Int) {
        inventory[posX, posY] = ItemStack.AIR
    }

    override fun isEmpty(): Boolean = inventory.itemStacks.all { it.isAir }

    override fun isNotEmpty(): Boolean = inventory.itemStacks.any { !it.isAir }

    override fun clear() = inventory.clear()

    override fun iterator(): Iterator<Pair<Int2, ItemStack>> = inventory.itemStacks.mapIndexed { index, itemStack ->
        Int2(index % size.x, index / size.y) to itemStack
    }.iterator()
}