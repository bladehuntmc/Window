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
import net.bladehunt.kotstom.dsl.item.item
import net.bladehunt.kotstom.dsl.item.itemName
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.dsl.*
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.window
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun main() = runBlocking {
    val server = MinecraftServer.init()

    val instance = InstanceManager.createInstanceContainer()
    val (firstMaterial, setFirstMaterial) = Signal(Material.DIAMOND)
    val (secondMaterial, setSecondMaterial) = Signal(Material.DIAMOND)

    GlobalEventHandler.listen<AsyncPlayerConfigurationEvent> { event ->
        event.spawningInstance = instance
        val player = event.player
        player.eventNode().listen<PlayerStartSneakingEvent> {
            val win = window(InventoryType.CHEST_6_ROW) {
                switch {
                    container {
                        column {
                            button {
                                size = Size2(0, 0)
                                display = {
                                    item(firstMaterial()) {}
                                }
                            }
                            row {
                                button {
                                    display = {
                                        item(if (this@switch.index == 0) Material.GREEN_WOOL else Material.RED_WOOL) {
                                            itemName = Component.text("First page")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        this@switch.index = 0
                                    }
                                }
                                button {
                                    display = {
                                        item(if (this@switch.index == 1) Material.GREEN_WOOL else Material.RED_WOOL) {
                                            itemName = Component.text("Second page")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        this@switch.index = 1
                                    }
                                }
                                button {
                                    display = {
                                        item(firstMaterial()) {
                                            itemName = Component.text("Change Item")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        setFirstMaterial(Material.values().random())
                                    }
                                }
                            }
                        }
                    }
                    container {
                        column {
                            button {
                                size = Size2(0, 0)
                                display = {
                                    item(secondMaterial()) {}
                                }
                            }
                            row {
                                button {
                                    display = {
                                        item(if (this@switch.index == 0) Material.GREEN_WOOL else Material.RED_WOOL) {
                                            itemName = Component.text("First page")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        this@switch.index = 0
                                    }
                                }
                                button {
                                    display = {
                                        item(if (this@switch.index == 1) Material.GREEN_WOOL else Material.RED_WOOL) {
                                            itemName = Component.text("Second page")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        this@switch.index = 1
                                    }
                                }
                                button {
                                    display = {
                                        item(secondMaterial()) {
                                            itemName = Component.text("Change Item")
                                        }
                                    }
                                    interaction = InteractionHandler { event ->
                                        setSecondMaterial(Material.values().random())
                                    }
                                }
                            }
                        }
                    }
                }
            }
            player.openInventory(win.inventory)
        }
    }
    server.start("127.0.0.1", 25565)
}