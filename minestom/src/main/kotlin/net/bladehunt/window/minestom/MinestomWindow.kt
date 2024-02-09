package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.Window
import net.bladehunt.window.core.component.Component
import net.bladehunt.window.core.util.Tree
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType

class MinestomWindow(type: InventoryType) : Window {
    val inventory = Inventory(type, AdventureComponent.text("Window"))
    var title get() = inventory.title
        set(title) {
            inventory.title = title
        }

    override val components: Tree<Component> = Tree(this)


    override fun onEvent(event: Event) {
        render()
    }
}
inline fun window(type: InventoryType, block: MinestomWindow.() -> Unit): MinestomWindow = MinestomWindow(type).apply(block).apply {
    render()
}