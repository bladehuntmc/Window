import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.minestom.AdventureComponent
import net.bladehunt.window.minestom.button
import net.bladehunt.window.minestom.title
import net.bladehunt.window.minestom.window
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.UUID

object WindowTest {
    lateinit var player: FakePlayer

    @BeforeAll @JvmStatic fun setup() {
        val server = MinecraftServer.init()

        val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
        MinecraftServer.getGlobalEventHandler().addListener(
            EventListener.of(PlayerLoginEvent::class.java) {
                it.setSpawningInstance(instance)
            }
        )

        server.start("127.0.0.1",12256)

        FakePlayer.initPlayer(UUID.randomUUID(), "fake_player") {
            player.setNoGravity(true)
            player.isAllowFlying = true
            player.isFlying = true
        }
    }

    @Test
    fun itemReactivity() {
        val signal = Signal("abc")
        window {
            type = InventoryType.CHEST_6_ROW
            title { AdventureComponent.text(signal()) }
            button {
                onClick = { player, result ->
                    var signalValue by signal
                    signalValue = "123"
                }
                item {
                    ItemStack.of(Material.STONE)
                        .withDisplayName(AdventureComponent.text(signal()))
                }
            }
        }
    }

    @AfterAll @JvmStatic fun complete() {
        MinecraftServer.stopCleanly()
    }
}