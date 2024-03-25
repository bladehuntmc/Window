package net.bladehunt.window.core

interface Parent<T> : Iterable<T> {
    val children: MutableCollection<T>

    override fun iterator(): Iterator<T> = children.iterator()
}