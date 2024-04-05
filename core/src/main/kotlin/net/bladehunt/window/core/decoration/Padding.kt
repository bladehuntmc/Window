package net.bladehunt.window.core.decoration

interface Padding<Pixel> {
    val top: Int
    val right: Int
    val left: Int
    val bottom: Int

    val pixel: Pixel

    data class Static<Pixel>(
        override val top: Int,
        override val right: Int,
        override val left: Int,
        override val bottom: Int,

        override val pixel: Pixel
    ) : Padding<Pixel> {
        constructor(amount: Int, item: Pixel) : this (amount, amount, amount, amount, item)
    }

    data class Dynamic<Pixel>(
        override val top: Int,
        override val right: Int,
        override val left: Int,
        override val bottom: Int,

        val block: () -> Pixel
    ) : Padding<Pixel> {
        constructor(amount: Int, itemBlock: () -> Pixel) : this (amount, amount, amount, amount, itemBlock)
        override val pixel: Pixel
            get() = block()
    }
}