package net.bladehunt.window.core.util

data class Tree<T>(val rootData: T?) {
    val rootNode = Node(rootData, null)

    data class Node<T>(val data: T?, val parent: Node<T>?, val children: MutableList<Node<T>> = mutableListOf()) {
        fun forEachDepthFirst(visit: Visitor<T>) {
            visit(this)
            children.forEach {
                it.forEachDepthFirst(visit)
            }
        }
    }

    fun forEachDepthFirst(visit: Visitor<T>) = rootNode.forEachDepthFirst(visit)
}

typealias Visitor<T> = (Tree.Node<T>) -> Unit