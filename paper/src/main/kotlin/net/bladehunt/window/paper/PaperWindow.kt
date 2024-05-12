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

package net.bladehunt.window.paper

import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.render.RenderContext
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class PaperWindow(
    plugin: Plugin,
    inventoryType: InventoryType,
    size: Int = inventoryType.defaultSize
) : Window<Interactable<ItemStack, InventoryInteractEvent>>(Size2(9, size / 9)) {
    private val interactionLayer = ArrayLayerImpl<InteractionHandler<InventoryInteractEvent>>(this.size.toInt2())

    val inventory = object : WindowInventory(plugin, inventoryType, Component.text("Window")) {
        override fun onEvent(event: InventoryEvent) {
            event as InventoryClickEvent
            interactionLayer[get2d(event.slot)]?.interact(event)
        }
    }

    override fun render() {
        val newContents = ArrayLayerImpl<Interactable<ItemStack, InventoryInteractEvent>>(size.toInt2())
        render(newContents, RenderContext(listOf()) { sizeX, sizeY -> ArrayLayerImpl(Int2(sizeX, sizeY)) })

        newContents.forEach { (pos, pixel) ->
            inventory.inventory.setItem(pos.x, pixel.pixel)
            val interactionHandler = pixel.interactionHandler ?: return@forEach
            interactionLayer[pos] = interactionHandler
        }
    }

    fun get2d(slot: Int): Int2 = Int2(slot % size.x, slot / size.x)
    fun getAbsolutePos(x: Int, y: Int): Int = y * size.x + x
}