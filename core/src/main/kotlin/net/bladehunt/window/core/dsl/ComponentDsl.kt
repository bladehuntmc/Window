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

package net.bladehunt.window.core.dsl

import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.layout.Column
import net.bladehunt.window.core.layout.Row
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.core.widget.Button
import net.bladehunt.window.core.widget.WidgetParent

@WindowDsl
inline fun <T> WidgetParent<T>.column(size: Size2 = Size2(), block: @WindowDsl Column<T>.() -> Unit): Column<T> = Column<T>(size).apply {
    block()
    this@column.addWidget(this)
}

@WindowDsl
inline fun <T> WidgetParent<T>.row(size: Size2 = Size2(), block: @WindowDsl Row<T>.() -> Unit): Row<T> = Row<T>(size).apply {
    block()
    this@row.addWidget(this)
}

@WindowDsl
inline fun <T, E> WidgetParent<Interactable<T, E>>.button(block: Button<T, E>.() -> Unit): Button<T, E> = Button<T, E>().also { button ->
    button.block()
    this@button.addWidget(button)
}