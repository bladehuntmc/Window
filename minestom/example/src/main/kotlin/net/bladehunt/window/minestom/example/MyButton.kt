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

import net.bladehunt.kotstom.dsl.item.item
import net.bladehunt.kotstom.dsl.item.lore
import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.core.dsl.button
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.util.FlexPair
import net.bladehunt.window.core.widget.Button
import net.bladehunt.window.core.widget.WidgetParent
import net.kyori.adventure.text.Component
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

val materials = Material.values().sortedBy { it.namespace().path() }

@WindowDsl
fun WidgetParent<Interactable<ItemStack, InventoryEvent>>.myButton(i: Int): Button<ItemStack, InventoryEvent> {
    val (index, setIndex) = Signal(i)
    return button {
        size = FlexPair(2, 2)
        display = {
            item(materials[index()]) {
                lore {
                    +Component.text("Click to change to")
                    +Component.text(materials[index()+1].namespace().path())
                }
            }
        }
        interaction = InteractionHandler { event ->
            event as PlayerEvent
            event.player.sendMessage("You clicked the ${materials[index()].namespace().path()}")
            setIndex(index() + 1)
        }
    }
}