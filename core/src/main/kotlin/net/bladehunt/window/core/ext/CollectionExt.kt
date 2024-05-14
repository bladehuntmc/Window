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

package net.bladehunt.window.core.ext

import net.bladehunt.window.core.util.PairedInts

operator fun <T> MutableMap<PairedInts, T>.set(x: Int, y: Int, value: T) {
    this[PairedInts(x, y)] = value
}
operator fun <T> MutableMap<PairedInts, T>.get(x: Int, y: Int): T? = this[PairedInts(
    x,
    y
)]

operator fun <T> Array<Array<T>>.set(x: Int, y: Int, value: T) {
    this[x][y] = value
}
operator fun <T> Array<Array<T>>.get(x: Int, y: Int, value: T) = this[x][y]


inline fun <T> Array<Array<T>>.forEach(block: (x: Int, y: Int, value: T?) -> Unit) {
    for (x in this.indices) {
        for (y in this[x].indices) {
            block(x, y, this[x][y])
        }
    }
}
inline fun <T> Array<Array<T?>>.forEachNotNull(block: (x: Int, y: Int, value: T) -> Unit) {
    forEach { x, y, value ->
        if (value == null) return@forEach
        block(x, y, value)
    }
}