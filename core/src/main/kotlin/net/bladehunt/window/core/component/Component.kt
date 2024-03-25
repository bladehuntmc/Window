package net.bladehunt.window.core.component

import net.bladehunt.reakt.reactivity.ReactiveContext
import net.bladehunt.window.core.canvas.Canvas

interface Component<Pixel> : ReactiveContext {
    fun render(canvas: Canvas<Pixel>)
}