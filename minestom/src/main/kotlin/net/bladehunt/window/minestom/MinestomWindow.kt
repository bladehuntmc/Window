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

import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.rowSize
import net.bladehunt.kotstom.extension.slots
import net.bladehunt.window.core.Context
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.Size
import net.bladehunt.window.minestom.inventory.InventoryLayer
import net.bladehunt.window.minestom.inventory.WindowInventory
import net.kyori.adventure.text.Component
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.timer.Task

class MinestomWindow(
    inventoryType: InventoryType,
    title: Component = Component.text("Window"),
) : Window<MinestomPixel>(Size(inventoryType.rowSize, inventoryType.size / inventoryType.rowSize)) {
    val inventory = WindowInventory(inventoryType, title)
    val interactionLayer =
        ArrayLayerImpl<InteractionHandler<InventoryPreClickEvent>>(size.toIntPair())

    init {
        inventory.eventNode().listen<InventoryPreClickEvent> { event: InventoryPreClickEvent ->
            val slots = event.clickInfo.slots
            when (slots.size) {
                0 -> {}
                1 -> {
                    val (slot) = slots
                    val clickedPosX = slot % inventory.inventoryType.rowSize
                    val clickedPosY = slot / inventory.inventoryType.rowSize
                    val handler = interactionLayer[clickedPosX, clickedPosY]
                    handler?.interact(event)
                    event.isCancelled = true
                }
                else -> {}
            }
        }
    }

    override val parentNode: Node<MinestomPixel> =
        Node(widget = this, size = size, context = Context())

    override fun render() {
        val reservation =
            inventory.transaction {
                buildNode(parentNode, parentNode.context)
                parentNode.layer = InventoryLayer(size.toIntPair(), it, interactionLayer)
                render(parentNode)
            }
    }

    override fun createArrayLayer(sizeX: Int, sizeY: Int): ArrayLayerImpl<MinestomPixel> =
        ArrayLayerImpl(IntPair(sizeX, sizeY))

    private var queue: Task? = null
}
