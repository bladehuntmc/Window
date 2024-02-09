package net.bladehunt.window.minestom.dsl

import net.bladehunt.window.core.dsl.Builder
import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.minestom.MinestomWindow
import net.bladehunt.window.minestom.component.Button
import net.minestom.server.entity.Player
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class ButtonBuilder(private val window: MinestomWindow) : Builder<Button>() {
    var slot: Button.() -> Int = { 0 }
    var item: Button.() -> ItemStack = { ItemStack.of(Material.STONE) }
    var onClick: (button: Button, player: Player, clickType: ClickType, result: InventoryConditionResult) -> Unit = { _, _, _, _ -> }
    override fun build(): Button = Button(window, slot, item, onClick)
}

fun MinestomWindow.button(
    block: @WindowDsl ButtonBuilder.() -> Unit
): Button = ButtonBuilder(this)
    .apply(block)
    .build()
    .also { addChild(it) }