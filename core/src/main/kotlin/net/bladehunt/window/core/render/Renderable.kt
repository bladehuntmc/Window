package net.bladehunt.window.core.render

import net.bladehunt.window.core.canvas.Canvas

interface Renderable<Pixel> {
    fun render(canvas: Canvas<Pixel>)
}