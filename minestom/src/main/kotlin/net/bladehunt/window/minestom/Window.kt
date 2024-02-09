package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.event.Event
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType

class Window(inventoryType: InventoryType) : Component {
    val tree = Tree<Component>(this)

    val inventory: Inventory = Inventory(
        inventoryType,
        AdventureComponent.text("Window")
    )

    override fun render() {
        TODO("Not yet implemented")
    }

    override fun onEvent(event: Event) {
        TODO("Not yet implemented")
    }
}