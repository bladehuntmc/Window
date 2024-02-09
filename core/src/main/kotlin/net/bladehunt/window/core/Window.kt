package net.bladehunt.window.core

import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.component.NodeComponent
import net.bladehunt.window.core.util.HandleContainer
import net.bladehunt.window.core.util.Tree

interface Window<T> : NodeComponent, HandleContainer<T> {
    val components: Tree<Component>

    override fun getNode(): Tree.Node<Component> = components.root
    override fun render() {
        renderChildren()
    }
    fun addChild(component: Component, parent: Component = getNode().data) {
        components.addChild(component, parent)
    }
    fun renderChildren(node: Tree.Node<Component> = components.root) {
        components.traversePreOrder(node) {
            if (it != this) it.render()
        }
    }
}