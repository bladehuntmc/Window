package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.minestom.MinestomWindow
import net.bladehunt.window.minestom.MinestomComponent

class Title(
    window: MinestomWindow,
    val title: Title.() -> AdventureComponent
) : MinestomComponent(window) {
    override fun render() {
        window.title = title()
    }
}