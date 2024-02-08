package net.bladehunt.window.minestom

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.item.WindowMaterial

class MinestomItem(
    override val material: WindowMaterial<NativeMaterial>,
    override val displayName: AdventureComponent,
    override val lore: MutableList<AdventureComponent>,
    override var amount: Int
) : WindowItem {
    override val handle: NativeItem
        get() = NativeItem.builder(material.handle)
            .displayName(displayName)
            .lore(lore)
            .amount(amount)
            .build()
}