package net.bladehunt.window.minestom.example

import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.decoration.Background
import net.bladehunt.window.core.decoration.Padding
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.component.*
import net.bladehunt.window.minestom.window
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.timer.TaskSchedule

fun main() {
    val server = MinecraftServer.init()

    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()

    val title = Signal(Component.text("Example"))
    val win = window(InventoryType.CHEST_6_ROW) {
        title { title() }
        container(
            background = Background.Static(ItemStack.of(Material.WHITE_STAINED_GLASS_PANE)),
            padding = Padding.Static(1, 1, 1, 0, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE))
        ) {
            column {
                staticItem(ItemStack.of(Material.RED_WOOL))
                staticItem(ItemStack.of(Material.GREEN_WOOL))
                staticItem(ItemStack.of(Material.BLUE_WOOL))
            }
        }
        row(size = Size2(y = 1)) {
            fill(ItemStack.of(Material.RED_STAINED_GLASS_PANE))
            staticItem(ItemStack.of(Material.BARRIER))
            fill(ItemStack.of(Material.RED_STAINED_GLASS_PANE))
        }
    }

    MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        event.spawningInstance = instance
        val player = event.player
        player.scheduleNextTick {
            player.eventNode().addListener(PlayerStartSneakingEvent::class.java) { sneakEvent ->
                player.openInventory(win.inventory)
            }
        }
        player.scheduler().scheduleTask({
            title.value = Component.text("Updated")
        }, TaskSchedule.seconds(5), TaskSchedule.stop())
    }

    server.start("127.0.0.1", 25565)
}