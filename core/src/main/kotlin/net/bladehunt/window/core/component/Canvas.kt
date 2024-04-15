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

package net.bladehunt.window.core.component

import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2
import kotlin.math.min

abstract class Canvas<Pixel>(override var size: Size2) : Component<Pixel>, ParentComponent<Pixel> {
    private val children: MutableCollection<Component<Pixel>> = arrayListOf()

    private val inner: MutableMap<Int2, Pixel> = hashMapOf()

    protected var innerSize: Canvas<Pixel>.() -> Int2 = { size.asInt2() }
    @WindowDsl fun innerSize(block: @WindowDsl Canvas<Pixel>.() -> Int2) { innerSize = block }

    protected var offset: Canvas<Pixel>.() -> Int2 = { Int2(0, 0) }
    @WindowDsl fun offset(block: @WindowDsl Canvas<Pixel>.() -> Int2) { offset = block }

    override fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel) {
        inner[pos] = pixel
        if (pos.x in 0..<size.x && pos.y in 0..<size.y) {
            reservation?.set(pos - offset(), pixel)
        }
    }

    override fun removeOne(component: Component<Pixel>, pos: Int2) {
        inner.remove(pos)
        if (pos.x in 0..<size.x && pos.y in 0..<size.y) {
            reservation?.remove(pos - offset())
        }
    }

    private fun updateReservation() {
        val (offsetX, offsetY) = offset()
        reservation?.also {
            for (x in 0..<size.x) {
                for (y in 0..<size.y) {
                    val item = inner[Int2(x + offsetX, y + offsetY)]
                    val reservedSlot = Int2(x, y)
                    if (item == null) {
                        if (it[reservedSlot] != null) {
                            it.remove(reservedSlot)
                        }
                        continue
                    }
                    it[reservedSlot] = item
                }
            }
        }
    }

    override fun onEvent(event: Event) {
        updateReservation()
    }

    override fun preRender(limits: Int2) {
        val first = firstOrNull() ?: return
        val inner = innerSize()
        first.preRender(inner)
        size = size.copy(
            x = if (size.flexX) min(first.size.x, limits.x) else size.x,
            y = if (size.flexY) min(first.size.y, limits.y) else size.y
        )
    }

    override fun render() {
        firstOrNull()?.render()
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Component<Pixel>> = children.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Component<Pixel>): Boolean = children.add(child)
}