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

package net.bladehunt.window.minestom.example.widgets

import java.lang.ref.WeakReference
import net.bladehunt.kotstom.dsl.item.invoke
import net.bladehunt.kotstom.dsl.item.item
import net.bladehunt.kotstom.dsl.item.itemName
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.minestom.widgets.dsl.button
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.minestom.MinestomWidget
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemComponent
import net.minestom.server.item.Material
import net.minestom.server.item.component.HeadProfile

@WindowDsl
fun playerDisplay(player: Player): MinestomWidget {
    val weakPlayer = WeakReference(player)

    return button {
        display =
            item(Material.PLAYER_HEAD) {
                itemName = player.name
                if (player.skin != null) ItemComponent.PROFILE(HeadProfile(player.skin!!))
            }
        onInteract = InteractionHandler { event ->
            val playerRef = weakPlayer.get()
            if (playerRef == null) {
                event.player.sendMessage("The player wasn't found.")
                return@InteractionHandler
            }

            event.player.sendMessage(
                playerRef.name +
                    " has".color(NamedTextColor.GRAY) +
                    " ${playerRef.health} HP".color(NamedTextColor.RED)
            )
        }
    }
}
