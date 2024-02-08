package net.bladehunt.window.core.dsl

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.Window

@WindowDsl
abstract class WindowBuilder<InventoryType, WindowType : Window> {
    var name: AdventureComponent? = null
    abstract var type: InventoryType

    abstract fun build(): WindowType
}