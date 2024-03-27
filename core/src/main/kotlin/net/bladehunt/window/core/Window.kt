package net.bladehunt.window.core

import net.bladehunt.window.core.canvas.Column
import net.bladehunt.window.core.canvas.Reservation
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.util.Size2

abstract class Window<Pixel>(override val size: Size2) : Column<Pixel, Component<Pixel>>, ParentComponent<Pixel> {
    override val reservation: Reservation<Pixel>
        get() = throw NotImplementedError("Cannot get a Reservation from a top level component")
    override val reservations: MutableMap<Component<Pixel>, Reservation<Pixel>> = linkedMapOf()
    override fun reserve(reserved: Component<Pixel>) {
        reservations[reserved] = reserved.reservation
    }

    override fun composite() {
        throw NotImplementedError("Cannot composite a top level component! Use Window<Pixel>#render")
    }

    override fun clear() = reservations.clear()
    override fun iterator(): Iterator<Component<Pixel>> = reservations.keys.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = reservations.remove(child) != null
    override fun addChild(child: Component<Pixel>): Boolean {
        reserve(child)
        return true
    }
}