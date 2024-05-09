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

import net.bladehunt.window.core.interact.Interaction
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.minestom.event.MinestomEvent
import net.minestom.server.item.ItemStack

class InventoryLayer(
    override val size: Int2,
    private val transaction: WindowInventory.Transaction,
) : Layer<Pair<ItemStack, Interaction<MinestomEvent>?>> {
    private val interactions = ArrayLayerImpl<Interaction<MinestomEvent>?>(size)

    override fun set(posX: Int, posY: Int, pixel: Pair<ItemStack, Interaction<MinestomEvent>?>) {
        transaction[getAbsoluteSlot(posX, posY)] = pixel.first
        interactions[posX, posY] = pixel.second
    }

    override fun get(posX: Int, posY: Int): Pair<ItemStack, Interaction<MinestomEvent>?> = transaction[getAbsoluteSlot(posX, posY)] to interactions[posX, posY]

    override fun remove(posX: Int, posY: Int) {
        transaction[getAbsoluteSlot(posX, posY)] = ItemStack.AIR
        interactions[posX, posY] = null
    }

    override fun isEmpty(): Boolean = transaction.isEmpty()
    override fun isNotEmpty(): Boolean = transaction.isNotEmpty()

    override fun isPositionEmpty(posX: Int, posY: Int): Boolean = transaction[getAbsoluteSlot(posX, posY)].isAir

    override fun clear() {
        transaction.clear()
        interactions.clear()
    }

    private fun getAbsoluteSlot(x: Int, y: Int): Int = y * size.x + x

    override fun iterator(): Iterator<Pair<Int2, Pair<ItemStack, Interaction<MinestomEvent>?>>> = transaction.itemStacks.mapIndexed { index, itemStack ->
        val slot = Int2(index % size.x, index / size.y)
        slot to (itemStack to interactions[slot])
    }.iterator()
}