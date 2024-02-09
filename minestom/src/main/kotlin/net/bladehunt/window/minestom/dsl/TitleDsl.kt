package net.bladehunt.window.minestom.dsl

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.minestom.MinestomWindow
import net.bladehunt.window.minestom.component.Title

fun MinestomWindow.title(
    block: @WindowDsl Title.() -> AdventureComponent
): Title = Title(this, block).also {
    addChild(it)
}