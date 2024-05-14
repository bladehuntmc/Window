/*
 * Copyright 2024 BladehuntMC
 * Copyright 2024 oglassdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package net.bladehunt.window.core

data class Context(
    private val contexts: Map<Class<*>, Any> = mapOf()
) {
    fun <T> useContext(clazz: Class<T>): T? = contexts[clazz]?.let { it as? T }

    inline fun <reified T> useContext() = useContext(T::class.java)

    fun <T> withoutContext(clazz: Class<T>): Context = copy(
        contexts = HashMap(contexts).also {
            it.remove(clazz)
        }
    )

    inline fun <reified T> withoutContext(): Context = withoutContext(T::class.java)

    fun <T> withContext(clazz: Class<T>, value: T): Context = copy(
        contexts = HashMap(contexts).also {
            it[clazz] = value
        }
    )

    inline fun <reified T> withContext(value: T): Context = withContext(T::class.java, value)
}