package net.bladehunt.window.minestom

import net.bladehunt.reakt.reactivity.Signal
import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.minestom.dsl.button
import net.bladehunt.window.minestom.dsl.title
import net.bladehunt.window.minestom.dsl.window
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

object WindowTest {
    @JvmStatic fun main(args: Array<String>) {
        val server = MinecraftServer.init()

        val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
        MinecraftServer.getGlobalEventHandler().addListener(
            EventListener.of(PlayerLoginEvent::class.java) { event ->
                event.setSpawningInstance(instance)
                val player = event.player
                player.scheduleNextTick {
                    player.setNoGravity(true)
                    player.isAllowFlying = true
                    player.isFlying = true

                    val signal = Signal(0)
                    val lastClicker = Signal<AdventureComponent>(AdventureComponent.empty())
                    val materials = Material.values().toList()
                    val window = window(InventoryType.CHEST_6_ROW) {
                        title { AdventureComponent.text("Dynamic title example: ${signal()}") }
                        button {
                            onClick = { button, player, clickType, result ->
                                var signalValue by signal
                                var lastClickerValue by lastClicker
                                signalValue += 1
                                lastClickerValue = player.name
                                result.isCancel = true
                            }
                            item = {
                                val signalValue by signal
                                ItemStack.builder(materials[signalValue])
                                    .displayName(AdventureComponent.text("Clicked $signalValue times"))
                                    .lore(
                                        AdventureComponent.text("Last clicked by: ")
                                            .append(lastClicker())
                                            .color(TextColor.color(0xffffff))
                                            .decoration(TextDecoration.ITALIC, false)
                                    )
                                    .build()
                            }
                        }
                    }
                    player.openInventory(window.handle)
                }
            }
        )

        server.start("127.0.0.1",25565)
    }
}