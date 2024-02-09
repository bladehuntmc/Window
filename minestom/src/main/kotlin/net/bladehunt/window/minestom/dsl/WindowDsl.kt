package net.bladehunt.window.minestom.dsl

import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.minestom.MinestomWindow
import net.minestom.server.inventory.InventoryType

inline fun window(
    type: InventoryType,
    block: @WindowDsl MinestomWindow.() -> Unit
): MinestomWindow = MinestomWindow(type)
    .apply(block)
    .apply { render() }