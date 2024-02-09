package net.bladehunt.window.minestom

import net.minestom.server.inventory.InventoryType

class WindowBuilder : Builder<Window, Unit>() {
    var type: InventoryType = InventoryType.CHEST_4_ROW
    private val components = arrayListOf<Builder<out ChildComponent, Window>>()

    fun addComponentBuilder(component: Builder<out ChildComponent, Window>) {
        components.add(component)
    }

    override fun build(value: Unit): Window {
        val window = Window(type)
        subscribe(window)
        components.forEach {
            window.tree.addChild(window, it.build(window))
        }
        window.render()
        return window
    }
}
inline fun window(block: WindowBuilder.() -> Unit): Window = WindowBuilder().also(block).build(Unit)