/*
 * Copyright 2024 BladehuntMC
 * Copyright 2024 oglassdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package net.bladehunt.minestom.example;

import net.bladehunt.window.minestom.MinestomWindow;
import net.bladehunt.window.minestom.widget.Button;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerStartSneakingEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class JavaExample {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        MinestomWindow window = new MinestomWindow(InventoryType.CHEST_6_ROW, Component.text("Window"));
        Button stoneButton = new Button(
                window.createReservation(Button.DEFAULT_SIZE),
                (button) -> ItemStack.of(Material.STONE),
                (button, event) -> {
                    event.getPlayer().sendMessage("You clicked the stone");
                    return null;
                }
        );
        window.addWidget(stoneButton);

        Button snowballButton = new Button(
                window.createReservation(Button.DEFAULT_SIZE),
                (button) -> ItemStack.of(Material.SNOWBALL),
                (button, event) -> {
                    event.getPlayer().sendMessage("You clicked the snowball");
                    return null;
                }
        );
        window.addWidget(snowballButton);

        window.render();

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);

            event.getPlayer().eventNode().addListener(PlayerStartSneakingEvent.class, sneakEvent -> {
                sneakEvent.getPlayer().openInventory(window.getInventory());
            });
        });

        server.start("127.0.0.1", 25565);
    }
}
