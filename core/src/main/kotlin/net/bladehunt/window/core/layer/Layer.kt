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

package net.bladehunt.window.core.layer

import net.bladehunt.window.core.util.Int2

interface Layer<Pixel> : Iterable<Pair<Int2, Pixel>> {
    val size: Int2

    operator fun set(posX: Int, posY: Int, pixel: Pixel)
    operator fun get(posX: Int, posY: Int): Pixel?

    operator fun set(pos: Int2, pixel: Pixel) = set(pos.x, pos.y, pixel)
    operator fun get(pos: Int2): Pixel? = get(pos.x, pos.y)

    fun remove(posX: Int, posY: Int)
    fun remove(pos: Int2) = remove(pos.x, pos.y)

    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean

    fun isPositionEmpty(posX: Int, posY: Int): Boolean
    fun isPositionEmpty(pos: Int2): Boolean = isPositionEmpty(pos.x, pos.y)

    fun clear()
}