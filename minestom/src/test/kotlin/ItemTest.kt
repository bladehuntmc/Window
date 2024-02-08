import net.bladehunt.reakt.reactivity.Effect
import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.minestom.reactiveItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.item.Material
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.UUID

object ItemTest {
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
        val names = arrayOf("Example", "Test", "123", "Abc")

        val nameSignal = Signal(names[0])
        val item = reactiveItem(Material.ACACIA_PLANKS) {
            displayName(Component.text(nameSignal()))
                .build()
        }
        var iter = 0
        Effect {
            val itemStack by item
            assertEquals((itemStack.displayName as? TextComponent)?.content(), names[iter])
            iter++
        }
        var name by nameSignal
        for (value in 1 until names.size) {
            name = names[value]
        }
    }

    @AfterAll @JvmStatic fun complete() {
        MinecraftServer.stopCleanly()
    }
}