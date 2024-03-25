package net.bladehunt.window.core.canvas

import net.bladehunt.window.core.util.Int2

sealed interface Size {
    val size: Int2

    val x: Int
        get() = size.x

    val y: Int
        get() = size.y

    operator fun contains(int2: Int2): Boolean = int2.x in 0..size.x && int2.y in 0..size.y
}

data class StaticSize(override val size: Int2) : Size

data class Flex(val minSize: Size, val maxSize: Size) : Size {
    override val size: Int2
        get() = TODO("Not yet implemented")
}