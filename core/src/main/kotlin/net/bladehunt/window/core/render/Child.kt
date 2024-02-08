package net.bladehunt.window.core.render

interface Child {
    val parent: Parent<out Child>
}