package net.bladehunt.window.core.component

import net.bladehunt.window.core.Parent
import net.bladehunt.window.core.util.Int2

interface ParentComponent<Pixel> : Component<Pixel>, Parent<Component<Pixel>> {
    fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel)
}