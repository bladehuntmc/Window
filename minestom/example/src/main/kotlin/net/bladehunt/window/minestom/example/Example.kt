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
import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.*
import net.bladehunt.window.minestom.component.button
import net.bladehunt.window.minestom.component.nav.navItem
import net.bladehunt.window.minestom.component.navbar
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.Enchantment
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun main() = runBlocking {
    val server = MinecraftServer.init()

    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()

    val materials = arrayOf(
        Material.WHITE_STAINED_GLASS_PANE,
        Material.LIGHT_GRAY_STAINED_GLASS_PANE,
        Material.GRAY_STAINED_GLASS_PANE
    )
    val titles = arrayOf(
        "First Example",
        "Second Example",
        "Third Example"
    )

    val backgroundMaterial = Signal(materials[0])
    val navbarMaterial = Signal(materials[1])
    val title = Signal(titles[0])

    val win = window(InventoryType.CHEST_6_ROW) {
        title { Component.text(title()) }
        container {
            background { ItemStack.of(backgroundMaterial()) }
            padding { Padding(1, 1, 1, 0, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)) }
            column {
                row {
                    size = Size2(y = 1)
                    materials.forEach { material ->
                        button {
                            item {
                                ItemStack.builder(material)
                                    .displayName(Component.text("Change background"))
                                    .meta { if (backgroundMaterial() == material) it.enchantment(Enchantment.EFFICIENCY, 1) }
                                    .build()
                            }
                            onClick = {
                                backgroundMaterial.value = material
                            }
                        }
                    }
                }
                row {
                    size = Size2(y = 1)
                    materials.forEach { material ->
                        button {
                            item {
                                ItemStack.builder(material)
                                    .displayName(Component.text("Change navbar"))
                                    .meta { if (navbarMaterial() == material) it.enchantment(Enchantment.EFFICIENCY, 1) }
                                    .build()
                            }
                            onClick = {
                                navbarMaterial.value = material
                            }
                        }
                    }
                }
                row {
                    size = Size2(y = 1)
                    titles.forEach { str ->
                        button {
                            item {
                                ItemStack.builder(Material.BOOK)
                                    .displayName(Component.text("Change navbar"))
                                    .lore(Component.text(str))
                                    .meta { if (title() == str) it.enchantment(Enchantment.EFFICIENCY, 1) }
                                    .build()
                            }
                            onClick = {
                                title.value = str
                            }
                        }
                    }
                }
            }
        }
        navbar {
            fill { ItemStack.of(navbarMaterial()) }
            navItem {
                display = {
                    ItemStack.of(Material.ARROW)
                }
            }
            navItem {
                display = {
                    ItemStack.of(Material.ARROW)
                }
            }
        }
    }

    MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        event.spawningInstance = instance
        val player = event.player
        player.scheduleNextTick {
            player.eventNode().addListener(PlayerStartSneakingEvent::class.java) { sneakEvent ->
                player.openInventory(win.inventory)
            }
        }
    }

    server.start("127.0.0.1", 25565)
}