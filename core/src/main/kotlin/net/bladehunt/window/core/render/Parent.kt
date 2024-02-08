package net.bladehunt.window.core.render

interface Parent<ChildType: Child> {
    val children: Collection<Child>
}