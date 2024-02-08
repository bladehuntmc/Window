package net.bladehunt.window.core.item

import net.bladehunt.window.core.AdventureComponent
import net.bladehunt.window.core.platform.NativeHandleContainer

interface WindowItem<NativeItem, NativeMaterial> : NativeHandleContainer<NativeItem> {
    val material: WindowMaterial<NativeMaterial>
    val displayName: AdventureComponent
    val lore: MutableList<AdventureComponent>
    val amount: Int
}