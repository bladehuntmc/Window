package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.minestom.MinestomWindow

class TitleComponent(
    window: MinestomWindow,
    val title: TitleComponent.() -> AdventureComponent
) : MinestomWindowComponent(window) {
    override fun render() {
        window.title = title()
    }
}
fun MinestomWindow.title(block: TitleComponent.() -> AdventureComponent): TitleComponent = TitleComponent(this, block)