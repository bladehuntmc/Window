package net.bladehunt.window.core.util

data class Int2(val x: Int, val y: Int) {
    operator fun plus(other: Int2): Int2 = Int2(x + other.x, y + other.y)
    operator fun minus(other: Int2): Int2 = Int2(x - other.x, y - other.y)

    operator fun times(other: Int2): Int2 = Int2(x * other.x, y * other.y)
}

operator fun <T> MutableMap<Int2, T>.set(x: Int, y: Int, value: T) {
    this[Int2(x, y)] = value
}
operator fun <T> MutableMap<Int2, T>.get(x: Int, y: Int): T? = this[Int2(x, y)]