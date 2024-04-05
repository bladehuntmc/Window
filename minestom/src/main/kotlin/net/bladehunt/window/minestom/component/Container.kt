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

package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Container as CoreContainer
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.decoration.Background
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack

@Suppress("UNCHECKED_CAST")
class Container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR),
    val defaultClickBehavior: (player: Player) -> Boolean = { true }
) : CoreContainer<ItemStack>(size, background, padding), InteractionHandler<MinestomInteraction> {
    override var reservation: Reservation<ItemStack>? = null

    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                val first = firstOrNull()

                event.result.isCancel = defaultClickBehavior(event.player)

                if (
                    first != null &&
                    event.clickPos.x in padding.left..<first.size.x + padding.left &&
                    event.clickPos.y in padding.top..<first.size.y + padding.top
                    ) {
                    try {
                        (first as? InteractionHandler<MinestomInteraction>)?.onEvent(
                            event.copy(
                                clickPos = event.clickPos.copy(x = event.clickPos.x - padding.left, y = event.clickPos.y - padding.top)
                            )
                        )
                    } catch (_: ClassCastException) {}
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@WindowDsl
inline fun ParentComponent<ItemStack>.container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR),
    noinline defaultClickBehavior: (player: Player) -> Boolean = { true },
    block: @WindowDsl Container.() -> Unit
): Container = Container(
    size,
    background,
    padding,
    defaultClickBehavior
)
    .also {
        it.block()
        it.reservation = ChildReservation(it, this)
        this.addChild(it)
    }