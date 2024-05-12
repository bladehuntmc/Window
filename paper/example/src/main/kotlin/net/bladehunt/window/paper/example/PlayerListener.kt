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

package net.bladehunt.window.paper.example

import net.bladehunt.window.core.dsl.button
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.paper.window
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack

object PlayerListener : Listener {
    @EventHandler
    fun onPlayerSneak(event: PlayerToggleSneakEvent) {
        val win = window(WindowPlugin.instance, InventoryType.CHEST) {
            button {
                display = {
                    ItemStack(Material.ACACIA_BUTTON)
                }
                interaction = InteractionHandler { event ->
                    event.whoClicked.sendMessage("You clicked the acacia button")
                }
            }
            button {
                display = {
                    ItemStack(Material.STONE)
                }
                interaction = InteractionHandler { event ->
                    event.whoClicked.sendMessage("You clicked the stone button")
                }
            }
        }
        event.player.openInventory(win.inventory)
    }
}