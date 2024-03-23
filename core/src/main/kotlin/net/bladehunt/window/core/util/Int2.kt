package net.bladehunt.window.core.util

data class Int2(val x: Int, val y: Int) {
    operator fun plus(other: Int2): Int2 = Int2(x + other.x, y + other.y)
    operator fun minus(other: Int2): Int2 = Int2(x - other.x, y - other.y)

    operator fun times(other: Int2): Int2 = Int2(x * other.x, y * other.y)
}