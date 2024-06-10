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

package net.bladehunt.window.core.widget

import net.bladehunt.window.core.Context
import net.bladehunt.window.core.ext.fill
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.layout.Window

abstract class AbstractButton<Pixel, Event> : Widget<Interactable<Pixel, Event>>(), Resizable {
    var onInteract by property<InteractionHandler<Event>>()

    abstract fun getDisplay(node: Window.Node<Interactable<Pixel, Event>>): Pixel

    override fun buildNode(parent: Window.Node<Interactable<Pixel, Event>>, context: Context) {
        parent.createChild(this, context)
    }

    override fun render(node: Window.Node<Interactable<Pixel, Event>>) {
        val layer = node.layer ?: throw IllegalStateException("The layer must not be null!")
        layer.fill(Interactable(getDisplay(node), onInteract))
    }
}
