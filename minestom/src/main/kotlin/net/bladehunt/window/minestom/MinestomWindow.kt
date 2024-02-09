package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.Window
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.core.util.Tree
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType

class MinestomWindow(type: InventoryType) : Window<Inventory> {
    override val handle = Inventory(type, AdventureComponent.text("Window"))
    var title get() = handle.title
        set(title) {
            handle.title = title
        }

    override val components: Tree<Component> = Tree(this)

    override fun onEvent(event: Event) {
        render()
    }
}