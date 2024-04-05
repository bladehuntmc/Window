package net.bladehunt.window.core.component

import net.bladehunt.window.core.WindowOverflowException
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

abstract class Column<Pixel>(override var size: Size2) : Component<Pixel>, ParentComponent<Pixel> {
    private val children: MutableCollection<Component<Pixel>> = arrayListOf()
    val offsets: MutableMap<Component<Pixel>, Int> = hashMapOf()

    override fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel) {
        val offset = offsets[component] ?: return
        reservation?.set(pos.copy(y = pos.y + offset), pixel)
    }

    override fun preRender(limits: Int2) {
        size = size.copy(
            x = if (size.flexX) limits.x else size.x,
            y = if (size.flexY) limits.y else size.y
        )

        val flexSpace = this.size.y - sumOf { if (!it.size.flexY) it.size.y else 0 }
        val flexItems = filter { it.size.flexY }
        if (flexItems.size > flexSpace) throw WindowOverflowException("There were too many components when trying to render the column")

        val each = if (flexItems.isNotEmpty()) flexSpace.floorDiv(flexItems.size) else 0
        var remainder = if (flexItems.isNotEmpty()) flexSpace % flexItems.size else 0

        forEach { component ->
            val size = component.size
            val sizeY = if (flexItems.contains(component)) {
                val sizeY = each + remainder.coerceIn(0, 1)
                remainder--
                sizeY
            } else size.y
            component.preRender(Int2(if (size.flexX) this.size.x else size.x, sizeY))
        }
    }

    override fun render() {
        var rows = 0
        forEach { component ->
            component.render()
            component.reservation?.forEach { (pos, pixel) ->
                reservation?.set(pos.copy(y = pos.y + rows), pixel)
            }
            offsets[component] = rows
            rows += component.size.y
        }
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Component<Pixel>> = children.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Component<Pixel>): Boolean = children.add(child)
}