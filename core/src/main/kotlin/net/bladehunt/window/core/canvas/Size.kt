package net.bladehunt.window.core.canvas

sealed interface Size {
    val x: Int
    val y: Int

    operator fun component1(): Int = x
    operator fun component2(): Int = y

    data class Static(override val x: Int, override val y: Int) : Size
    data class Flex(override var x: Int = 0, override var y: Int = 0) : Size
}