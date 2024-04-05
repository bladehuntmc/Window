package net.bladehunt.window.core.util

data class Size2(val x: Int, val flexX: Boolean, val y: Int, val flexY: Boolean) {
    constructor(x: Int? = null, y: Int? = null) : this(
        x ?: 0,
        x == null,
        y ?: 0,
        y == null,
    )
    fun asInt2(): Int2 = Int2(x, y)
}