package net.bladehunt.window.core

interface Parent<T> : Iterable<T> {
    fun addChild(child: T): Boolean
    fun removeChild(child: T): Boolean
    fun clear()
    override fun iterator(): Iterator<T>
}