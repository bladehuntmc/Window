package net.bladehunt.window.core.component

import net.bladehunt.window.core.WindowOverflowException
import net.bladehunt.window.core.util.Int2
import net.bladehunt.window.core.util.Size2

abstract class Row<Pixel>(override var size: Size2) : Component<Pixel>, ParentComponent<Pixel> {
    private val children: MutableCollection<Component<Pixel>> = arrayListOf()
    private val offsets: MutableMap<Component<Pixel>, Int> = hashMapOf()

    override fun updateOne(component: Component<Pixel>, pos: Int2, pixel: Pixel) {
        val offset = offsets[component] ?: return
        reservation?.set(pos.copy(x = pos.x + offset), pixel)
    }

    override fun preRender(limits: Int2) {
        size = size.copy(
            x = if (size.flexX) limits.x else size.x,
            y = if (size.flexY) limits.y else size.y
        )

        val flexSpace = this.size.x - sumOf { if (!it.size.flexX) it.size.x else 0 }
        val flexItems = filter { it.size.flexX }
        if (flexItems.size > flexSpace) throw WindowOverflowException("There were too many components when trying to render the row")

        val each = if (flexItems.isNotEmpty()) flexSpace.floorDiv(flexItems.size) else 0
        var remainder = if (flexItems.isNotEmpty()) flexSpace % flexItems.size else 0

        forEach { component ->
            val size = component.size
            val sizeX = if (flexItems.contains(component)) {
                val sizeX = each + remainder.coerceIn(0, 1)
                remainder--
                sizeX
            } else size.x
            component.preRender(Int2(sizeX, if (size.flexY) this.size.y else size.y))
        }
    }

    override fun render() {
        var columns = 0
        forEach { component ->
            component.render()
            component.reservation?.forEach { (pos, pixel) ->
                reservation?.set(pos.copy(x = pos.x + columns), pixel)
            }
            offsets[component] = columns
            columns += component.size.x
        }
    }

    override fun clear() = children.clear()
    override fun iterator(): Iterator<Component<Pixel>> = children.iterator()
    override fun removeChild(child: Component<Pixel>): Boolean = children.remove(child)
    override fun addChild(child: Component<Pixel>): Boolean = children.add(child)
}
