package net.bladehunt.window.core.dsl

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.item.WindowItem

abstract class ItemBuilder<MaterialType, ItemType>(val material: MaterialType) {
    var displayName: AdventureComponent? = null
    var lore: MutableList<AdventureComponent> = arrayListOf()
    var amount: Int = 1

    abstract fun build(): WindowItem<ItemType>
}