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

import net.bladehunt.window.core.util.IntPair

class OffsetLimitedLayer<Pixel>(
    val parent: Layer<Pixel>,
    val offsetX: Int,
    val offsetY: Int,
    override val size: IntPair
) : Layer<Pixel> {
    override fun get(posX: Int, posY: Int): Pixel? {
        if (posX < 0 || posX >= size.x || posY < 0 || posY >= size.y) {
            throw IllegalArgumentException("Position ($posX, $posY) is out of bounds")
        }
        return parent[posX + offsetX, posY + offsetY]
    }

    override fun remove(posX: Int, posY: Int) {
        if (posX < 0 || posX >= size.x || posY < 0 || posY >= size.y) {
            throw IllegalArgumentException("Position ($posX, $posY) is out of bounds")
        }
        parent.remove(posX + offsetX, posY + offsetY)
    }

    override fun isEmpty(): Boolean {
        for (x in 0 until size.x) {
            for (y in 0 until size.y) {
                if (parent[x + offsetX, y + offsetY] != null) {
                    return false
                }
            }
        }
        return true
    }

    override fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    override fun isPositionEmpty(posX: Int, posY: Int): Boolean {
        val x = posX + offsetX
        val y = posY + offsetY
        if (x < 0 || x >= size.x || y < 0 || y >= size.y) {
            throw IllegalArgumentException("Position ($posX, $posY) is out of bounds")
        }
        return parent.isPositionEmpty(x, y)
    }

    override fun copyTo(other: Layer<Pixel>) {
        for (x in 0..<size.x) {
            for (y in 0..<size.y) {
                val value = parent[x + offsetX, y + offsetY]
                if (value == null) other.remove(x, y)
                else other[x, y] = value
            }
        }
    }

    override fun clear() {
        for (x in 0 until size.x) {
            for (y in 0 until size.y) {
                parent.remove(x + offsetX, y + offsetY)
            }
        }
    }

    override fun set(posX: Int, posY: Int, pixel: Pixel) {
        if (posX < 0 || posX >= size.x || posY < 0 || posY >= size.y) {
            throw IllegalArgumentException("Position ($posX, $posY) is out of bounds")
        }
        parent[posX + offsetX, posY + offsetY] = pixel
    }

    override fun iterator(): Iterator<Pair<IntPair, Pixel>> {
        val parentIterator = parent.iterator()
        return object : Iterator<Pair<IntPair, Pixel>> {
            override fun hasNext(): Boolean {
                return parentIterator.hasNext()
            }

            override fun next(): Pair<IntPair, Pixel> {
                val (pos, pixel) = parentIterator.next()
                val x = pos.x + offsetX
                val y = pos.y + offsetY
                if (x < 0 || x >= size.x || y < 0 || y >= size.y) {
                    throw IllegalArgumentException("Position ($x, $y) is out of bounds")
                }
                return IntPair(x, y) to pixel
            }
        }
    }
}