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

import net.bladehunt.window.core.Context
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layer.ArrayLayerImpl
import net.bladehunt.window.core.layout.Window
import net.bladehunt.window.core.util.IntPair
import net.bladehunt.window.core.util.Size
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PaperWindow(
    private val plugin: Plugin,
    inventoryType: InventoryType = InventoryType.CHEST,
    rowSize: Int = 9,
    size: Int = 27,
    title: Component = Component.empty()
) :
    InventoryHolder,
    Window<Interactable<ItemStack, InventoryClickEvent>>(
        Size(rowSize, inventoryType.defaultSize / rowSize)
    ) {

    private val inventory: Inventory =
        if (inventoryType == InventoryType.CHEST) plugin.server.createInventory(this, size, title)
        else plugin.server.createInventory(this, inventoryType, title)

    override fun getInventory(): Inventory = inventory

    private val interactionLayer =
        ArrayLayerImpl<InteractionHandler<InventoryClickEvent>>(this.size.toPairedInts())
    override val parentNode: Node<Interactable<ItemStack, InventoryClickEvent>> =
        Node(widget = this, size = this.size)

    override fun createArrayLayer(
        sizeX: Int,
        sizeY: Int
    ): ArrayLayerImpl<Interactable<ItemStack, InventoryClickEvent>> =
        ArrayLayerImpl(IntPair(sizeX, sizeY))

    override fun render() {
        val newContents =
            ArrayLayerImpl<Interactable<ItemStack, InventoryClickEvent>>(size.toPairedInts())
        render(Phase.BuildPhase(this, Context(), parentNode))
        render(Phase.RenderPhase(this, Context(), parentNode, newContents))
        newContents.forEach { (pos, pixel) ->
            inventory.setItem(getAbsolutePos(pos.x, pos.y), pixel.pixel)
            val interactionHandler = pixel.interactionHandler ?: return@forEach
            interactionLayer[pos] = interactionHandler
        }
    }

    fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true
        interactionLayer[get2d(event.slot)]?.interact(event)
    }

    private var runnable: BukkitRunnable? = null

    override fun requestUpdate() {
        if (runnable != null) return
        runnable =
            object : BukkitRunnable() {
                override fun run() {
                    render()
                    runnable = null
                }
            }
        runnable!!.runTaskLater(plugin, 1)
    }

    private fun get2d(slot: Int): IntPair = IntPair(slot % size.x, slot / size.x)

    private fun getAbsolutePos(x: Int, y: Int): Int = y * size.x + x
}
