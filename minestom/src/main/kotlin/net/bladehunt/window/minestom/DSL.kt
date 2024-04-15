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

@file:JvmName("DSL")

package net.bladehunt.window.minestom

import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.component.*
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

@WindowDsl
inline fun window(type: InventoryType, block: @WindowDsl MinestomWindow.() -> Unit): MinestomWindow = MinestomWindow(
    MinestomInventoryReservation(Inventory(type, "Window"))
)
    .apply {
        block()
        preRender(this.size.asInt2())
        render()
    }

@WindowDsl
inline fun ParentComponent<ItemStack>.column(
    size: Size2 = Size2(),
    block: @WindowDsl Column.() -> Unit
): Column = Column(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}

@WindowDsl
inline fun ParentComponent<ItemStack>.row(
    size: Size2 = Size2(),
    block: @WindowDsl Row.() -> Unit
): Row = Row(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}

@WindowDsl
inline fun ParentComponent<ItemStack>.auto(
    size: Size2 = Size2(),
    block: @WindowDsl Auto.() -> Unit
): Auto = Auto(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}

@WindowDsl
inline fun ParentComponent<ItemStack>.container(
    size: Size2 = Size2(),
    block: @WindowDsl Container.() -> Unit
): Container = Container(
    size
).also {
    it.block()
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}

@WindowDsl
fun ParentComponent<ItemStack>.fill(item: ItemStack): Fill = Fill(item).also {
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}

@WindowDsl
fun ParentComponent<ItemStack>.item(item: ItemStack): Item = Item { item } .also {
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}
@WindowDsl
fun ParentComponent<ItemStack>.item(item: Item.() -> ItemStack): Item = Item(item).also {
    it.reservation = ChildReservation(it, this)
    this.addChild(it)
}