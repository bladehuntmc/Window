import net.bladehunt.window.minestom.MinestomRightClick
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.UUID

object WindowItemTest {
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

    }

    @AfterAll @JvmStatic fun complete() {
        MinecraftServer.stopCleanly()
    }
}