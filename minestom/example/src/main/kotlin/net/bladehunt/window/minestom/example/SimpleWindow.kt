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

package net.bladehunt.window.minestom.example

import kotlinx.coroutines.runBlocking
import net.bladehunt.kotstom.GlobalEventHandler
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.interact.Interaction
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.minestom.dsl.button
import net.bladehunt.window.minestom.dsl.window
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun main() = runBlocking {
    val server = MinecraftServer.init()

    val instance = InstanceManager.createInstanceContainer()
    val (material, setMaterial) = Signal(Material.DIAMOND)
    val (size, setSize) = Signal(Int2(1, 1))

    val win = window(InventoryType.CHEST_6_ROW) {
        button {
            itemStack = {
                ItemStack.of(Material.BOOK)
            }
            interaction = {
                Interaction { event ->
                    event.player.sendMessage("You clicked the book")
                }
            }
        }
        button {
            itemStack = {
                ItemStack.of(Material.SNOW)
            }
            interaction = {
                Interaction { event ->
                    val newSize = if (size().y == 1) Int2(2, 2) else Int2(1, 1)
                    setSize(newSize)
                    event.player.sendMessage("You clicked the snow")
                }
            }
            finalSize = {
                size()
            }
        }
        button {
            itemStack = {
                ItemStack.of(material())
            }
            interaction = {
                Interaction { event ->
                    event.player.sendMessage("You clicked the ${material().name().lowercase()}")
                    setMaterial(Material.values().random())
                }
            }
        }
    }

    GlobalEventHandler.listen<AsyncPlayerConfigurationEvent> { event ->
        event.spawningInstance = instance
        val player = event.player
        player.eventNode().listen<PlayerStartSneakingEvent> {
            player.openInventory(win.inventory)
        }
    }
    server.start("127.0.0.1", 25565)
}