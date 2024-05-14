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

import net.bladehunt.window.core.Phase
import net.bladehunt.window.core.interact.Interactable
import net.bladehunt.window.core.interact.InteractionHandler
import net.bladehunt.window.core.util.FlexPair

class Button<Pixel, Event>(
    var display: (Button<Pixel, Event>.() -> Pixel)? = null,
    var interaction: InteractionHandler<Event>? = null,
    size: FlexPair = FlexPair(
        1,
        1
    )
) : Widget<Interactable<Pixel, Event>>(), Resizable {
    override fun render(phase: Phase<Interactable<Pixel, Event>>) {
        when (phase) {
            is Phase.BuildPhase -> {}
            is Phase.RenderPhase -> {
                for (x in 0..<phase.layer.size.x) {
                    for (y in 0..<phase.layer.size.y) {
                        phase.layer[x, y] = Interactable(display!!.invoke(this), interaction)
                    }
                }
                isDirty = false
            }
        }
    }

    override var size: FlexPair = size
        set(value) {
            isDirty = true
            field = value
            requestUpdate()
        }
}