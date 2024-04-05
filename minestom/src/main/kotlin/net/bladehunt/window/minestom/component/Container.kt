package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.WindowDsl
import net.bladehunt.window.core.component.Container as CoreContainer
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.decoration.Background
import net.bladehunt.window.core.interaction.InteractionHandler
import net.bladehunt.window.core.reservation.ChildReservation
import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.MinestomInteraction
import net.minestom.server.entity.Player
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack

@Suppress("UNCHECKED_CAST")
class Container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR),
    val defaultClickBehavior: (player: Player) -> Boolean = { true }
) : CoreContainer<ItemStack>(size, background, padding), InteractionHandler<MinestomInteraction> {
    override var reservation: Reservation<ItemStack>? = null

    override fun onEvent(event: MinestomInteraction) {
        when (event) {
            is MinestomInteraction.InventoryCondition -> {
                val first = firstOrNull()

                event.result.isCancel = defaultClickBehavior(event.player)

                if (
                    first != null &&
                    event.clickPos.x in padding.left..<first.size.x + padding.left &&
                    event.clickPos.y in padding.top..<first.size.y + padding.top
                    ) {
                    try {
                        (first as? InteractionHandler<MinestomInteraction>)?.onEvent(
                            event.copy(
                                clickPos = event.clickPos.copy(x = event.clickPos.x - padding.left, y = event.clickPos.y - padding.top)
                            )
                        )
                    } catch (_: ClassCastException) {}
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@WindowDsl
inline fun ParentComponent<ItemStack>.container(
    size: Size2 = Size2(),
    background: Background<ItemStack> = Background.None as Background<ItemStack>,
    padding: Padding<ItemStack> = Padding.Static(0, ItemStack.AIR),
    noinline defaultClickBehavior: (player: Player) -> Boolean = { true },
    block: @WindowDsl Container.() -> Unit
): Container = Container(
    size,
    background,
    padding,
    defaultClickBehavior
)
    .also {
        it.block()
        it.reservation = ChildReservation(it, this)
        this.addChild(it)
    }