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

package net.bladehunt.window.core.decoration

interface Padding<Pixel> {
    val top: Int
    val right: Int
    val left: Int
    val bottom: Int

    val pixel: Pixel

    data class Static<Pixel>(
        override val top: Int,
        override val right: Int,
        override val left: Int,
        override val bottom: Int,

        override val pixel: Pixel
    ) : Padding<Pixel> {
        constructor(amount: Int, item: Pixel) : this (amount, amount, amount, amount, item)
    }

    data class Dynamic<Pixel>(
        override val top: Int,
        override val right: Int,
        override val left: Int,
        override val bottom: Int,

        val block: () -> Pixel
    ) : Padding<Pixel> {
        constructor(amount: Int, itemBlock: () -> Pixel) : this (amount, amount, amount, amount, itemBlock)
        override val pixel: Pixel
            get() = block()
    }
}