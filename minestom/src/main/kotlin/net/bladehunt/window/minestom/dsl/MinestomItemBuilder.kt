package net.bladehunt.window.minestom.dsl

import net.bladehunt.window.core.dsl.ItemBuilder
import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.core.item.WindowItem
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

@WindowDsl
class MinestomItemBuilder(material: Material) : ItemBuilder<Material, ItemStack>(material) {
    override fun build(): WindowItem<ItemStack> {
        TODO("Not yet implemented")
    }
}