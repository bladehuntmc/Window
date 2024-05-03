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
import net.bladehunt.window.minestom.MinestomWindow
import net.bladehunt.window.minestom.widget.Button
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun main() = runBlocking {
    val server = MinecraftServer.init()

    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()

    val win = MinestomWindow(InventoryType.CHEST_6_ROW)

    win.addWidget(
        Button(
            { ItemStack.of(Material.STONE) },
            { event ->
                event.player.sendMessage("You clicked the stone")
            },
            win.createReservation(Button.DEFAULT_SIZE)
        )
    )
    win.addWidget(
        Button(
            { ItemStack.of(Material.SNOWBALL) },
            { event ->
                event.player.sendMessage("You clicked the snowball")
            },
            win.createReservation(Button.DEFAULT_SIZE)
        )
    )

    win.render()

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