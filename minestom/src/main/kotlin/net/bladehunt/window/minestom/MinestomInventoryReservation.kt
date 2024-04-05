package net.bladehunt.window.minestom

import net.bladehunt.window.core.reservation.Reservation
import net.bladehunt.window.core.util.Int2
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

class MinestomInventoryReservation(val inventory: Inventory) : Reservation<ItemStack> {
    override fun set(slot: Int2, pixel: ItemStack) {
        inventory[slot] = pixel
    }

    override fun get(slot: Int2): ItemStack = inventory[slot]

    override fun isEmpty(): Boolean = inventory.itemStacks.all { it.isAir }

    override fun isNotEmpty(): Boolean = inventory.itemStacks.any { !it.isAir }

    override fun clear() = inventory.clear()

    override fun iterator(): Iterator<Pair<Int2, ItemStack>> {
        val rowSize = inventory.inventoryType.rowSize
        return iterator {
            for (slot in 0..inventory.inventoryType.size) {
                val item = inventory[slot]
                if (item.isAir) continue
                yield(Int2(slot % rowSize, slot / rowSize) to item)
            }
        }
    }
}