package net.bladehunt.window.core.decoration

interface Background<Pixel> {
    val item: Pixel?

    data object None : Background<Nothing> {
        override val item: Nothing? = null
    }

    data class Static<Pixel>(
        override val item: Pixel
    ) : Background<Pixel>

    data class Dynamic<Pixel>(
        val itemBlock: () -> Pixel?
    ) : Background<Pixel?> {
        override val item: Pixel?
            get() = itemBlock()
    }
}