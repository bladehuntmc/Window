package net.bladehunt.window.core.component

import net.bladehunt.window.core.canvas.*
import net.bladehunt.window.core.canvas.Column

class Column<Pixel, Parent>(val parent: Parent, size: Size) : ParentComponent<Pixel>, Column<Pixel>(size) where Parent : ParentComponent<Pixel>, Parent : Canvas<Pixel> {

    override val children: MutableCollection<Component<Pixel>> = arrayListOf()
    override fun render() {
        parent.reserve(this)
    }
}