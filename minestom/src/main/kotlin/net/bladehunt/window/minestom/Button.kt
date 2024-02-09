package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.event.Event
import net.minestom.server.entity.Player
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack

class Button(
    override val window: Window,
    val item: Button.() -> ItemStack,
    val onClick: Button.(player: Player, result: InventoryConditionResult) -> Unit
) : ChildComponent {
    override fun render() {

    }
    override fun onEvent(event: Event) {
        render()
    }
}
class ButtonBuilder : Builder<Button, Window>() {
    private var item: Button.() -> ItemStack = {
        throw RuntimeException("A button's item must be set!")
    }
    fun item(block: Button.() -> ItemStack) {
        item = block
    }
    var onClick: Button.(player: Player, result: InventoryConditionResult) -> Unit = { player, result ->
        result.isCancel = true
    }
    override fun build(value: Window): Button {
        return Button(value, item, onClick)
    }
}
fun WindowBuilder.button(block: ButtonBuilder.() -> Unit): ButtonBuilder {
    val builder = ButtonBuilder().apply(block)
    this.addComponentBuilder(builder)
    return builder
}