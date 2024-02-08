package net.bladehunt.window.core

import net.bladehunt.window.core.item.WindowItem
import net.bladehunt.window.core.render.Renderable
import net.bladehunt.window.core.util.Tree

interface Window<NativeItem, NativeMaterial> : Renderable {
    val components: Tree<WindowItem<NativeItem, NativeMaterial>>
}