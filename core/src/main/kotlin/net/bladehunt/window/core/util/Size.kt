package net.bladehunt.window.core.util

sealed interface Size {
    val amount: Int
    operator fun component1(): Int = amount

    data class Static(override val amount: Int) : Size
    data class Flex(override var amount: Int = 0) : Size
}