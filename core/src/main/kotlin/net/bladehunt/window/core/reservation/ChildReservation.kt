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

package net.bladehunt.window.core.reservation

import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Int2

class ChildReservation<Pixel>(
    private val onSet: (pos: Int2, pixel: Pixel) -> Unit,
    private val onRemove: (pos: Int2) -> Unit
) : Reservation<Pixel> {

    constructor(component: Component<Pixel>, parent: ParentComponent<Pixel>) : this(
        { pos: Int2, pixel: Pixel ->
            parent.updateOne(component, pos, pixel)
        },
        { pos: Int2 ->
            parent.removeOne(component, pos)
        }
    )

    private val pixelMap = hashMapOf<Int2, Pixel>()
    override fun set(slot: Int2, pixel: Pixel) {
        pixelMap[slot] = pixel
        onSet(slot, pixel)
    }

    override fun remove(slot: Int2) {
        pixelMap.remove(slot)
        onRemove(slot)
    }

    override fun get(slot: Int2): Pixel? = pixelMap[slot]

    override fun isEmpty(): Boolean = pixelMap.isEmpty()

    override fun isNotEmpty(): Boolean = pixelMap.isNotEmpty()

    override fun clear() {
        pixelMap.forEach { (pos, _) ->
            onRemove(pos)
        }
        pixelMap.clear()
    }

    override fun iterator(): Iterator<Pair<Int2, Pixel>> = pixelMap.map { it.toPair() }.iterator()
}