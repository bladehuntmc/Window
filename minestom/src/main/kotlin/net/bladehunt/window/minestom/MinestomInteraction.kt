package net.bladehunt.window.minestom

import net.bladehunt.window.core.util.Int2
import net.minestom.server.entity.Player
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryConditionResult

sealed interface MinestomInteraction {
    /**
     * @property player The player that clicked
     * @property slot The original slot that was clicked
     * @property clickPos The position clicked relative to the offset of the starting position
     * @property type The ClickType that was performed
     * @property result The result - Allows cancellation of the event
     */
    data class InventoryCondition(
        val player: Player,
        val slot: Int2,
        val clickPos: Int2,
        val type: ClickType,
        val result: InventoryConditionResult
    ) : MinestomInteraction
}