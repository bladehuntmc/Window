package net.bladehunt.window.core.inventory

import net.bladehunt.window.core.item.WindowItem
import net.bladehunt.window.core.platform.NativeHandleContainer

interface WindowInventory<
        NativeInventoryType,
        NativeInventoryTypeType,
        ItemType : WindowItem<*,*>
        > : NativeHandleContainer<NativeInventoryType> {
    val type: NativeInventoryTypeType

    /**
     * Will register any listeners needed for the Window
     */
    fun registerListeners() {}

    /**
     * Sets an item in an inventory
     *
     * @param slot The slot of the inventory to set
     * @param item If item is null, the item should be set to AIR
     */
    operator fun set(slot: Int, item: ItemType?)

    /**
     *
     */
    operator fun get(slot: Int): ItemType
}