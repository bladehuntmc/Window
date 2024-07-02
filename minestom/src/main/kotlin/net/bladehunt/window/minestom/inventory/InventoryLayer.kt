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

import net.bladehunt.kotstom.extension.get
import net.bladehunt.kotstom.extension.rowSize
import net.bladehunt.kotstom.extension.set
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.minestom.MinestomPixel
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

class InventoryLayer(val inventory: Inventory) : Layer<MinestomPixel> {
    override val size: IntPair =
        IntPair(
            inventory.inventoryType.rowSize,
            inventory.inventoryType.size / inventory.inventoryType.rowSize
        )

    private val interactionHandlers =
        ArrayLayerImpl<InteractionHandler<InventoryPreClickEvent>?>(size)

    override fun get(posX: Int, posY: Int): MinestomPixel {
        return MinestomPixel(inventory[posX, posY], interactionHandlers[posX, posY])
    }

    override fun remove(posX: Int, posY: Int) {
        inventory[posX, posY] = ItemStack.AIR
        interactionHandlers.remove(posX, posY)
    }

    override fun isEmpty(): Boolean =
        inventory.itemStacks.all { it.isAir } && interactionHandlers.isEmpty()

    override fun isNotEmpty(): Boolean =
        inventory.itemStacks.any { !it.isAir } && interactionHandlers.isNotEmpty()

    override fun isPositionEmpty(posX: Int, posY: Int): Boolean {
        return inventory[posX, posY].isAir && interactionHandlers[posX, posY] == null
    }

    override fun clear() {
        inventory.clear()
        interactionHandlers.clear()
    }

    override fun copyTo(other: Layer<MinestomPixel>) {
        throw UnsupportedOperationException("Can't copy an inventory layer")
    }

    override fun set(posX: Int, posY: Int, pixel: MinestomPixel) {
        inventory[posX, posY] = pixel.pixel
        interactionHandlers[posX, posY] = pixel.interactionHandler
    }

    override fun iterator(): Iterator<Pair<IntPair, MinestomPixel>> =
        object : Iterator<Pair<IntPair, MinestomPixel>> {
            private var currentIndexX = 0
            private var currentIndexY = 0

            override fun hasNext(): Boolean {
                return currentIndexY < size.y && currentIndexX < size.x
            }

            override fun next(): Pair<IntPair, MinestomPixel> {
                val next =
                    IntPair(currentIndexX, currentIndexY) to get(currentIndexX, currentIndexY)
                currentIndexX++
                if (currentIndexX >= size.x) {
                    currentIndexX = 0
                    currentIndexY++
                }
                return next
            }
        }
}
