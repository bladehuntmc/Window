package net.bladehunt.window.core.util

data class Size2(val x: Size, val y: Size) {
    constructor(x: Int = -1, y: Int = -1) : this(
        if (x < 0) Size.Flex() else Size.Static(x),
        if (y < 0) Size.Flex() else Size.Static(y)
    )
}