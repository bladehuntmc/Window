package net.bladehunt.window.minestom

import net.bladehunt.window.core.Window
import net.bladehunt.window.core.interact.Interaction
import net.bladehunt.window.core.item.WindowItem
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

// Native types
typealias NativeInventory = Inventory
typealias NativeInventoryType = InventoryType
typealias NativePlayer = Player
typealias NativeItem = ItemStack
typealias NativeMaterial = Material

// Window types
typealias Window = Window<NativeItem, NativeMaterial>
typealias WindowItem = WindowItem<NativeItem, NativeMaterial>

// Interactions
typealias MinestomRightClick = Interaction.RightClick<MinestomWindow, NativePlayer, NativeItem>
typealias MinestomMiddleClick = Interaction.MiddleClick<MinestomWindow, NativePlayer, NativeItem>
typealias MinestomLeftClick = Interaction.LeftClick<MinestomWindow, NativePlayer, NativeItem>
typealias MinestomSneakClick = Interaction.SneakClick<MinestomWindow, NativePlayer, NativeItem>
typealias MinestomOuterClick = Interaction.OuterClick<MinestomWindow, NativePlayer>
typealias MinestomItemSpread = Interaction.ItemSpread<MinestomWindow, NativePlayer, NativeItem>