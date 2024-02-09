package net.bladehunt.window.minestom.component

import net.bladehunt.reakt.pubsub.EventPublisher
import net.bladehunt.reakt.pubsub.event.Event
import net.bladehunt.window.core.component.NodeComponent
import net.bladehunt.window.core.component.WindowComponent
import net.bladehunt.window.core.dsl.Builder
import net.bladehunt.window.minestom.MinestomWindow
import net.minestom.server.entity.Player
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryCondition
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class ButtonInventoryCondition(
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

class Button(
    window: MinestomWindow,
    private val slot: Button.() -> Int,
    private val item: Button.() -> ItemStack,
    private val onClick: (button: Button, player: Player, clickType: ClickType, result: InventoryConditionResult) -> Unit
) : MinestomWindowComponent(window) {
    private var previousCondition: ButtonInventoryCondition? = null

    override fun render() {
        val slot = slot()
        previousCondition?.let { window.inventory.inventoryConditions.remove(it) }
        ButtonInventoryCondition(
            this,
            slot,
            onClick
        ).also {
            previousCondition = it
            window.inventory.addInventoryCondition(it)
        }
        window.inventory.setItemStack(slot, item())
    }
}
class ButtonBuilder(private val window: MinestomWindow) : Builder<Button>() {
    var slot: Button.() -> Int = { 0 }
    var item: Button.() -> ItemStack = { ItemStack.of(Material.STONE) }
    var onClick: (button: Button, player: Player, clickType: ClickType, result: InventoryConditionResult) -> Unit = { _, _, _, _ -> }
    override fun build(): Button = Button(window, slot, item, onClick)
}
fun MinestomWindow.button(block: ButtonBuilder.() -> Unit): Button = ButtonBuilder(this).apply(block).let {
    val built = it.build()
    getNode().addChild(built)
    built
}