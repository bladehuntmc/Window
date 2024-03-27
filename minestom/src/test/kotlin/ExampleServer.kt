import net.bladehunt.window.core.canvas.container.Padding
import net.bladehunt.window.core.util.Size2
import net.bladehunt.window.minestom.component.*
import net.bladehunt.window.minestom.window
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun main() {
    val server = MinecraftServer.init()

    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()

    val win = window(InventoryType.CHEST_6_ROW) {
        container(padding = Padding.Static(1, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE))) {
            staticItem(ItemStack.of(Material.STONE))
        }
    }
    win.iterator().forEach {
        println(it)
    }
    win.inventory.itemStacks.forEach {
        println(it)
    }

    MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        event.spawningInstance = instance
        val player = event.player
        player.scheduleNextTick {
            player.eventNode().addListener(PlayerStartSneakingEvent::class.java) { sneakEvent ->
                player.openInventory(win.inventory)
            }
        }
    }

    server.start("127.0.0.1", 25565)
}