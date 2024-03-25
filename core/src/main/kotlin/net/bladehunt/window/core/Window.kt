package net.bladehunt.window.core

import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.canvas.Shaped
import net.bladehunt.window.core.canvas.Size
import net.bladehunt.window.core.canvas.StaticSize
import net.bladehunt.window.core.component.Column
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Int2

class Window<Pixel>(size: Size) : ParentComponent<Pixel>, net.bladehunt.window.core.canvas.Column<Pixel>(size) {
    override fun render() {
        TODO("Not yet implemented")
    }

    override val children: MutableCollection<Component<Pixel>> = mutableListOf()
    override val reservations: MutableList<Pair<Shaped, Reservation<Pixel>>> = arrayListOf()

    override fun composite(): Map<Int2, Pixel> {
        TODO("Not yet implemented")
    }
}

fun test() {
    val window = Window<Int>(StaticSize(Int2(5, 5)))
    val a = Column(window, StaticSize(Int2(2, 5)))
    a.render()
}