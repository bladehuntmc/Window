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
import net.bladehunt.window.core.Column
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.event.MinestomEvent
import net.bladehunt.window.minestom.inventory.InventoryLayer
import net.bladehunt.window.minestom.inventory.WindowInventory
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule

class MinestomWindow(
    inventoryType: InventoryType,
    title: Component = Component.text("Window"),
) : Column<WindowItem>() {
    val inventory = WindowInventory(inventoryType, title)
    private var listener: (InventoryPreClickEvent) -> Unit = {}

    init {
        MinecraftServer.getGlobalEventHandler().listen<InventoryPreClickEvent> { event ->
            listener(event)
        }
    }

    fun render() {
        inventory.transaction { transaction ->
            //transaction.clear()
            val size = Int2(
                inventory.inventoryType.rowSize,
                inventory.size / inventory.inventoryType.rowSize
            )
            val layer = InventoryLayer(
                size,
                transaction
            )
            onRender(layer)
            listener = { event: InventoryPreClickEvent ->
                val slots = event.clickInfo.slots
                when (slots.size) {
                    0 -> {}
                    1 -> {
                        val (slot) = slots
                        layer[slot % inventory.inventoryType.rowSize, slot / inventory.inventoryType.rowSize].second?.interact(
                            MinestomEvent.PreClickEvent(event)
                        )
                        event.isCancelled = true
                    }
                    else -> {}
                }
            }
        }
    }

    // Queue section
    private var queue: Task? = null
    override fun requestUpdate() {
        if (queue == null) {
            queue = runnable {
                delay = TaskSchedule.nextTick()
                run {
                    render()
                    queue = null
                }
            }.schedule()
        }
    }

    override val size: Size2 = Size2(inventoryType.rowSize, inventoryType.size / inventoryType.rowSize)
}