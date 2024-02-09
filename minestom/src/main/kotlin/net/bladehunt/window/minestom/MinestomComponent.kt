package net.bladehunt.window.minestom

import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.WindowComponent
import net.bladehunt.window.core.util.Tree

abstract class MinestomComponent(
    override val window: MinestomWindow
) : WindowComponent<MinestomWindow> {
    override fun getNode(): Tree.Node<Component>? = window.components.findNode(this)
}