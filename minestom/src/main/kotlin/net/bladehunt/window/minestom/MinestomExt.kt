package net.bladehunt.window.minestom

import net.bladehunt.reakt.reactivity.Memo
import net.bladehunt.reakt.reactivity.MemoResult
import net.bladehunt.reakt.reactivity.ReactiveContext
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

fun reactiveItem(
    material: Material,
    block: context(ReactiveContext) ItemStack.Builder.(memoResult: MemoResult) -> ItemStack
): Memo<ItemStack> {
    return Memo { block(ItemStack.builder(material), it) }
}