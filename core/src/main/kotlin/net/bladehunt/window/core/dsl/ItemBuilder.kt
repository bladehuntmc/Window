package net.bladehunt.window.core.dsl

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.item.WindowItem

abstract class ItemBuilder<NativeMaterial, NativeItem>(
    val material: NativeMaterial
) {
    var displayName: AdventureComponent? = null
    var lore: MutableList<AdventureComponent> = arrayListOf()
    var amount: Int = 1

    abstract fun build(): WindowItem<NativeItem>
}