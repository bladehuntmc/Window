package net.bladehunt.window.core

import net.bladehunt.window.core.canvas.Column
import net.bladehunt.window.core.canvas.Row
import net.bladehunt.window.core.canvas.Size
import net.bladehunt.window.core.util.Int2

fun main() {
    Column<Int>(Size.Static(5, 5)).apply {
        Row<Int>(Size.Flex()).let {
            reserve(it).let { (map) ->
                map[Int2(0, 0)] = 1
            }
        }
        Row<Int>(Size.Flex()).let {
            reserve(it).let { (map) ->
                map[Int2(0, 0)] = 2
            }
        }
        Row<Int>(Size.Static(5, 2)).let {
            reserve(it).let { (map) ->
                map[Int2(0, 0)] = 3
            }
        }

        println(composite())
    }
}