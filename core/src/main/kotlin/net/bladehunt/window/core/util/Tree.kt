package net.bladehunt.window.core.util

class Tree<T>(rootData: T) {
    val root = Node(rootData)

    data class Node<T>(
        val data: T,
        val parent: Node<T>? = null,
        val children: MutableList<Node<T>> = mutableListOf()
    ) {
        fun addChild(child: T) {
            children.add(Node(child, this))
        }

        fun findNode(value: T): Node<T>? {
            if (data == value) return this
            for (child in children) {
                child.findNode(value)?.let { return it }
            }
            return null
        }

        fun remove() {
            parent?.children?.remove(this)
        }
    }

    fun addChild(parentData: T, childData: T) {
        findNode(parentData)?.addChild(childData)
    }

    fun findNode(value: T): Node<T>? = root.findNode(value)

    fun removeNode(value: T) {
        findNode(value)?.remove()
    }

    fun traversePreOrder(node: Node<T> = root, visit: (T) -> Unit) {
        visit(node.data)
        for (child in node.children) {
            traversePreOrder(child, visit)
        }
    }

    fun traversePostOrder(node: Node<T> = root, visit: (T) -> Unit) {
        for (child in node.children) {
            traversePostOrder(child, visit)
        }
        visit(node.data)
    }

    fun traverseLevelOrder(visit: (T) -> Unit) {
        val queue = ArrayDeque<Node<T>>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            visit(node.data)
            queue.addAll(node.children)
        }
    }
}