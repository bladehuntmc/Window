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

package net.bladehunt.window.core

import net.bladehunt.window.core.layer.Layer
import net.bladehunt.window.core.layout.Window

sealed interface Phase<T> {
    val window: Window<T>
    val context: Context

    /**
     * In this phase, nodes should be appended to the parent.
     */
    data class BuildPhase<T>(
        override val window: Window<T>,
        override val context: Context,
        val node: Window.Node<T>
    ) : Phase<T>

    /**
     * In this phase, widgets should be rendered to the layer
     * At this point, the size of the node shouldn't be changed.
     */
    data class RenderPhase<T>(
        override val window: Window<T>,
        override val context: Context,
        val node: Window.Node<T>,
        val layer: Layer<T>
    ) : Phase<T>

    fun component1(): Window<T>
    fun component2(): Context
    fun component3(): Window.Node<T>
}