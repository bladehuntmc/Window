package net.bladehunt.window.core

abstract class Window<Pixel> : Parent<Pixel> {
    override val children: MutableCollection<Pixel> = mutableListOf()
}