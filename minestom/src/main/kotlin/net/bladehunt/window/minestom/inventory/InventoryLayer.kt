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

import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.Int2
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.item.ItemStack

class InventoryLayer(
    override val size: Int2,
    private val transaction: WindowInventory.Transaction,
    private val interactions: Layer<InteractionHandler<InventoryEvent>>
) : Layer<Interactable<ItemStack, InventoryEvent>> {

    override fun set(posX: Int, posY: Int, pixel: Interactable<ItemStack, InventoryEvent>) {
        transaction[getAbsoluteSlot(posX, posY)] = pixel.pixel

        if (pixel.interactionHandler != null) {
            interactions[posX, posY] = pixel.interactionHandler!!
        } else interactions.remove(posX, posY)
    }

    override fun get(posX: Int, posY: Int): Interactable<ItemStack, InventoryEvent> = Interactable(transaction[getAbsoluteSlot(posX, posY)], interactions[posX, posY])

    override fun remove(posX: Int, posY: Int) {
        transaction[getAbsoluteSlot(posX, posY)] = ItemStack.AIR
        interactions.remove(posX, posY)
    }

    override fun isEmpty(): Boolean = transaction.isEmpty()
    override fun isNotEmpty(): Boolean = transaction.isNotEmpty()

    override fun isPositionEmpty(posX: Int, posY: Int): Boolean = transaction[getAbsoluteSlot(posX, posY)].isAir

    override fun copyTo(other: Layer<Interactable<ItemStack, InventoryEvent>>) {
        throw UnsupportedOperationException("This operation is not supported for inventory layers")
    }

    override fun clear() {
        transaction.clear()
        interactions.clear()
    }

    private fun getAbsoluteSlot(x: Int, y: Int): Int = y * size.x + x

    override fun iterator(): Iterator<Pair<Int2, Interactable<ItemStack, InventoryEvent>>> = iterator {
        for (x in 0..<size.x) {
            for (y in 0..<size.y) {
                yield(Int2(x, y) to Interactable(transaction[getAbsoluteSlot(x, y)], interactions[x, y]))
            }
        }
    }
}