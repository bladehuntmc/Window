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

package net.bladehunt.minestom.components

import net.bladehunt.window.core.Phase
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Widget
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class Navbar(
    private val orientation: Orientation = Orientation.Horizontal,
    private val style: Style = Style.Normal,
    private val closePosition: Position = Position.Center
) : Widget<Interactable<ItemStack, InventoryEvent>>() {
    private val items: MutableList<Interactable<ItemStack, InventoryEvent>> = arrayListOf()
    fun addItem(item: Interactable<ItemStack, InventoryEvent>) {
        items.add(item)
    }

    enum class Orientation {
        Vertical,
        Horizontal
    }
    enum class Style {
        Normal,
        Compact
    }
    enum class Position {
        Start,
        Center,
        End
    }
    override val size: Size2 = when (orientation) {
        Orientation.Vertical -> Size2(1, 0)
        Orientation.Horizontal -> Size2(0, 1)
    }
    override fun render(phase: Phase<Interactable<ItemStack, InventoryEvent>>) {
        when (phase) {
            is Phase.BuildPhase -> {}
            is Phase.RenderPhase -> {
                val layer = phase.layer
                val length: Int = when (orientation) {
                    Orientation.Vertical -> layer.size.y
                    Orientation.Horizontal -> layer.size.x
                }
                val setItem: (Int, Interactable<ItemStack, InventoryEvent>) -> Unit = when (orientation) {
                    Orientation.Vertical -> ({ pos, pixel -> layer[0, pos] = pixel })
                    Orientation.Horizontal -> ({ pos, pixel -> layer[pos, 0] = pixel })
                }
                for (i in 0..<length) {
                    setItem(
                        i,
                        items.getOrNull(i) ?: Interactable(ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)) {}
                    )
                }
            }
        }
    }
}