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
import net.bladehunt.kotstom.dsl.runnable
import net.bladehunt.kotstom.extension.rowSize
import net.bladehunt.kotstom.extension.slots
import net.bladehunt.window.core.Context
import net.bladehunt.window.core.Phase
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.inventory.InventoryLayer
import net.bladehunt.window.minestom.inventory.WindowInventory
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.timer.ExecutionType
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule

class MinestomWindow(
    inventoryType: InventoryType,
    title: Component = Component.text("Window"),
) : Window<Interactable<ItemStack, InventoryEvent>>(Size2(inventoryType.rowSize, inventoryType.size / inventoryType.rowSize)) {
    val inventory = WindowInventory(inventoryType, title)
    val interactionLayer = ArrayLayerImpl<InteractionHandler<InventoryEvent>>(size.toInt2())

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

    override val parentNode: Node<Interactable<ItemStack, InventoryEvent>> = Node(widget = this, size = size)

    override fun createArrayLayer(sizeX: Int, sizeY: Int): ArrayLayerImpl<Interactable<ItemStack, InventoryEvent>> = ArrayLayerImpl(Int2(sizeX, sizeY))

    override fun render() {
        inventory.transaction { transaction ->
            transaction.clear()
            val size = size.toInt2()
            interactionLayer.clear()
            val layer = InventoryLayer(
                size,
                transaction,
                interactionLayer
            )
            val start = System.nanoTime()
            render(Phase.BuildPhase(this, Context(), parentNode))
            render(Phase.RenderPhase(this, Context(), parentNode, layer))
            println("Took ${(System.nanoTime() - start)/1000000.0}ms to render")
        }
    }

    private var queue: Task? = null
    override fun requestUpdate() {
        if (queue == null) {
            queue = runnable {
                delay = TaskSchedule.immediate()
                executionType = ExecutionType.TICK_END
                run {
                    render()
                    queue = null
                }
            }.schedule()
        }
    }

}