package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.WindowComponent
import net.bladehunt.window.core.util.Tree
import net.bladehunt.window.minestom.MinestomWindow

abstract class MinestomWindowComponent(
    override val window: MinestomWindow
) : WindowComponent<MinestomWindow> {
    override fun getNode(): Tree.Node<Component>? = window.components.findNode(this)
}