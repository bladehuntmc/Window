package net.bladehunt.window.core.interaction

interface InteractionHandler<T> {
    fun onEvent(event: T)
}