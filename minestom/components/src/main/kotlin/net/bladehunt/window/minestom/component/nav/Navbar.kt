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

package net.bladehunt.window.minestom.component.nav

import net.bladehunt.window.core.Parent
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import kotlin.math.max

class Navbar : Component<ItemStack>, Parent<NavItem>, InteractionHandler<MinestomInteraction> {
    private var layout: Navbar.() -> Layout = { Layout.HORIZONTAL }
    @WindowDsl fun layout(block: @WindowDsl Navbar.() -> Layout) { layout = block }

    private var itemPosition: Navbar.() -> ItemPosition = { ItemPosition.CENTER }
    @WindowDsl fun itemPosition(block: @WindowDsl Navbar.() -> ItemPosition) { itemPosition = block }

    private var closeStyle: Navbar.() -> CloseStyle = { CloseStyle.CENTER }
    @WindowDsl fun closeStyle(block: @WindowDsl Navbar.() -> CloseStyle) { closeStyle = block }

    private var fill: Navbar.() -> ItemStack = { ItemStack.of(Material.RED_STAINED_GLASS_PANE) }
    @WindowDsl fun fill(block: @WindowDsl Navbar.() -> ItemStack) { fill = block }

    enum class Layout {
        HORIZONTAL,
        VERTICAL
    }
    enum class ItemPosition {
        START,
        CENTER,
        END
    }
    enum class CloseStyle {
        DISABLED,
        CENTER,
        LEFT,
        RIGHT
    }
    override var size: Size2 = layout().let {
        Size2(
            if (it == Layout.VERTICAL) 1 else null,
            if (it == Layout.HORIZONTAL) 1 else null
        )
    }
        private set

    override var reservation: Reservation<ItemStack>? = null

    override fun preRender(limits: Int2) {
        val layout = layout()
        size = Size2(
            if (layout == Layout.VERTICAL) 1 else limits.x,
            if (layout == Layout.HORIZONTAL) 1 else limits.y
        )
    }

    private fun setOrdered(pos: Int, item: ItemStack) {
        val layout = layout()
        val int2 = Int2(
            if (layout == Layout.VERTICAL) 0 else pos,
            if (layout == Layout.HORIZONTAL) 0 else pos
        )
        reservation?.set(int2, item)
    }

    override fun render() {
        val layout = layout()
        val closeStyle = closeStyle()
        val len = max(size.x, size.y)
        for (i in 0..<len) {
            setOrdered(
                i,
                fill()
            )
        }
        when (closeStyle) {
            CloseStyle.DISABLED -> {}
            CloseStyle.CENTER -> {
                val center = len / 2
                setOrdered(center, ItemStack.of(Material.BARRIER))
                val offset = center - children.size / 2
                for (i in 0..<children.size) {
                    val child = children[i]
                    val withOffset = i + offset
                    setOrdered(if (withOffset < center) withOffset else withOffset + 1, child.display(this))
                }
            }
            CloseStyle.LEFT -> {
                setOrdered(0, ItemStack.of(Material.BARRIER))
                for (i in 0..<children.size) {
                    val child = children[i]
                    setOrdered(i + 1, child.display(this))
                }
            }
            CloseStyle.RIGHT -> {
                setOrdered(len - 1, ItemStack.of(Material.BARRIER))
                for (i in 0..<children.size) {
                    val child = children[i]
                    setOrdered(i, child.display(this))
                }
            }
        }
    }

    private val children: MutableList<NavItem> = arrayListOf()
    override fun addChild(child: NavItem): Boolean = children.add(child)
    override fun removeChild(child: NavItem): Boolean = children.remove(child)
    override fun clear() = children.clear()
    override fun iterator(): Iterator<NavItem> = children.iterator()
    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                event.result.isCancel = true
            }
        }
    }
}