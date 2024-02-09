package net.bladehunt.window.minestom

import net.bladehunt.reakt.pubsub.event.Event

class Title(
    override val window: Window,
    val block: Title.() -> AdventureComponent
) : ChildComponent {

    override fun render() {
        window.inventory.title = block()
    }

    override fun onEvent(event: Event) {
        render()
    }
}
@WindowDsl
class TitleBuilder(
    private val block: Title.() -> AdventureComponent
) : Builder<Title, Window>() {
    override fun build(value: Window): Title {
        return Title(value, block)
    }
}

fun WindowBuilder.title(block: Title.() -> AdventureComponent): TitleBuilder {
    val builder = TitleBuilder(block)
    this.addComponentBuilder(builder)
    return builder
}