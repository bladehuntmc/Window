package net.bladehunt.window.minestom.component

import net.bladehunt.window.minestom.MinestomWindow
import net.bladehunt.window.minestom.MinestomComponent
import net.minestom.server.entity.Player
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryCondition
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack

class Button(
    window: MinestomWindow,
    private val slot: Button.() -> Int,
    private val item: Button.() -> ItemStack,
    private val onClick: (button: Button, player: Player, clickType: ClickType, result: InventoryConditionResult) -> Unit
) : MinestomComponent(window) {
    private var previousCondition: ButtonCondition? = null

    override fun render() {
        val slot = slot()
        previousCondition?.let { window.inventory.inventoryConditions.remove(it) }
        ButtonCondition(
            this,
            slot,
            onClick
        ).also {
            previousCondition = it
            window.inventory.addInventoryCondition(it)
        }
        window.inventory.setItemStack(slot, item())
    }
    class ButtonCondition(
        private val button: Button,
        private val slot: Int,
        private val block: Button.(player: Player, clickType: ClickType, result: InventoryConditionResult) -> Unit
    ) : InventoryCondition {
        override fun accept(
            player: Player,
            slot: Int,
            clickType: ClickType,
            inventoryConditionResult: InventoryConditionResult
        ) {
            if (slot != this.slot) return
            button.block(player, clickType, inventoryConditionResult)
        }
    }
}