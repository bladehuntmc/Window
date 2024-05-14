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

import net.bladehunt.window.core.exception.WindowException
import net.bladehunt.window.core.ext.forEach
import net.bladehunt.window.core.ext.forEachNotNull
import net.bladehunt.window.core.util.PairedInts

inline fun <reified T> ArrayLayerImpl(size: PairedInts): ArrayLayerImpl<T> = ArrayLayerImpl(
    size,
    Array(size.x) { Array(size.y) { null } }
)
class ArrayLayerImpl<Pixel>(
    size: PairedInts,
    private var pixels: Array<Array<Pixel?>>
) : Layer<Pixel> {
    override var size: PairedInts = size
        private set

    override fun get(posX: Int, posY: Int): Pixel? = pixels[posX][posY]

    override fun set(posX: Int, posY: Int, pixel: Pixel) {
        pixels[posX][posY] = pixel
    }

    override fun remove(posX: Int, posY: Int) {
        pixels[posX][posY] = null
    }

    override fun isEmpty(): Boolean = pixels.all { pixelArray -> pixelArray.all { pixel -> pixel == null } }

    override fun isNotEmpty(): Boolean = pixels.any { pixelArray -> pixelArray.any { pixel -> pixel != null } }

    override fun isPositionEmpty(posX: Int, posY: Int): Boolean = pixels[posX][posY] == null

    override fun clear() {
        pixels.forEach { x, y, _ ->
            pixels[x][y] = null
        }
    }

    override fun copyTo(other: Layer<Pixel>) {
        if (other.size != size) throw WindowException("Layers must be the same size to copy")
        pixels.forEach { x, y, value ->
            if (value == null) other.remove(x, y)
            else other[x, y] = value
        }
    }

    override fun iterator(): Iterator<Pair<PairedInts, Pixel>> {
        return iterator {
            pixels.forEachNotNull { x, y, value ->
                yield(PairedInts(x, y) to value)
            }
        }
    }
}