package net.bladehunt.window.core

import net.bladehunt.window.core.item.WindowItem
import net.bladehunt.window.core.util.Tree

interface Window {
    val components: Tree<WindowItem<*>>
}