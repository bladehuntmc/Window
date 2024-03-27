package net.bladehunt.window.minestom

import net.bladehunt.window.core.Window
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.util.Size2
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

class MinestomWindow(inventoryType: InventoryType) : Window<ItemStack>(
    Size2(inventoryType.rowSize, inventoryType.size / inventoryType.rowSize)
) {
    val inventory = Inventory(inventoryType, "Window").also {
        it.addInventoryCondition { _, _, _, inventoryConditionResult ->
            inventoryConditionResult.isCancel = true
        }
    }

    override fun render() {
        calculateSizes()
        iterator().forEach {
            it.render()
        }
        build().forEach { (pos, item) ->
            inventory[pos] = item
        }
    }
}
@WindowDsl
inline fun window(type: InventoryType, block: @WindowDsl MinestomWindow.() -> Unit): MinestomWindow = MinestomWindow(type)
    .apply {
        block()
        render()
    }