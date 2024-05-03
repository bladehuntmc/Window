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

import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

inline fun <reified T> ArrayReservationImpl(size: Size2): ArrayReservationImpl<T> = ArrayReservationImpl(size) { sizeX, sizeY ->
    Array(sizeX) { Array(sizeY) { null } }
}
class ArrayReservationImpl<Pixel>(
    size: Size2,
    private val arrayFactory: (sizeX: Int, sizeY: Int) -> Array<Array<Pixel?>>
) : Reservation<Pixel>, Resizable {
    override var size: Size2 = size
        private set
    constructor(size: Size2, pixelClass: Class<Pixel>) : this(
        size,
        { sizeX, sizeY ->
            java.lang.reflect.Array.newInstance(pixelClass, sizeX, sizeY) as Array<Array<Pixel?>>
        }
    )

    private var pixels: Array<Array<Pixel?>> = arrayFactory(size.x, size.y)
    override val usedSize: Int2
        get() {
            var totalX = 0
            var totalY = 0
            pixels.forEach { x, y, value ->
                if (value == null) return@forEach
                if (x > totalX) totalX = x
                if (y > totalX) totalY = y
            }
            return Int2(
                if (size.flexX) totalX else size.x,
                if (size.flexY) totalY else size.y,
            )
        }

    override fun resize(sizeX: Int, sizeY: Int) {
        if (sizeX < 0 || sizeY < 0) throw IllegalArgumentException("Size dimensions must be non-negative")

        val newArray = arrayFactory(sizeX, sizeY)
        pixels.forEachNotNull { x, y, value ->
            if (x >= sizeX || y >= sizeY) return@forEachNotNull
            newArray[x][y] = value
        }
        pixels = newArray
        this.size = Size2(sizeX, this.size.flexX, sizeY, this.size.flexY)
    }

    override fun get(posX: Int, posY: Int): Pixel? = pixels[posX][posY]

    override fun set(posX: Int, posY: Int, pixel: Pixel) {
        pixels[posX][posY] = pixel
    }

    override fun remove(posX: Int, posY: Int) {
        pixels[posX][posY] = null
    }

    override fun isEmpty(): Boolean = pixels.all { pixelArray -> pixelArray.all { pixel -> pixel == null } }

    override fun isNotEmpty(): Boolean = pixels.any { pixelArray -> pixelArray.any { pixel -> pixel != null } }

    override fun clear() {
        pixels.forEach { x, y, _ ->
            pixels[x][y] = null
        }
    }

    override fun iterator(): Iterator<Pair<Int2, Pixel>> {
        return iterator {
            pixels.forEachNotNull { x, y, value ->
                yield(Int2(x, y) to value)
            }
        }
    }
    private inline fun Array<Array<Pixel?>>.forEach(block: (x: Int, y: Int, value: Pixel?) -> Unit) {
        for (x in this.indices) {
            for (y in this[x].indices) {
                block(x, y, this[x][y])
            }
        }
    }
    private inline fun Array<Array<Pixel?>>.forEachNotNull(block: (x: Int, y: Int, value: Pixel) -> Unit) {
        forEach { x, y, value ->
            if (value == null) return@forEach
            block(x, y, value)
        }
    }
}