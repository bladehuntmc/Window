package net.bladehunt.window.core.util

data class Size2(val x: Int = 0, val flexX: Boolean = true, val y: Int = 0, val flexY: Boolean = true) {
    constructor(x: Int? = null, y: Int? = null) : this(
        x ?: 0,
        x == null,
        y ?: 0,
        y == null,
    )
}