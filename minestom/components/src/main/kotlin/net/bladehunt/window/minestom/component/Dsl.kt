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

package net.bladehunt.window.minestom.component

import net.bladehunt.window.core.dsl.WindowDsl
import net.bladehunt.window.core.component.ParentComponent
import net.bladehunt.window.core.dsl.applyWindowDsl
import net.bladehunt.window.core.interaction.InteractableParentComponent
import net.bladehunt.window.minestom.MinestomInteraction
import net.bladehunt.window.minestom.component.nav.Navbar
import net.minestom.server.item.ItemStack

@WindowDsl
inline fun ParentComponent<ItemStack>.navbar(block: @WindowDsl Navbar.() -> Unit) = applyWindowDsl(Navbar(), block)
@WindowDsl
inline fun InteractableParentComponent<MinestomInteraction, ItemStack>.navbar(
    block: @WindowDsl Navbar.() -> Unit
) = applyWindowDsl(Navbar(), block)

@WindowDsl
inline fun ParentComponent<ItemStack>.button(block: @WindowDsl Button.() -> Unit) = applyWindowDsl(Button(), block)
@WindowDsl
inline fun InteractableParentComponent<MinestomInteraction, ItemStack>.button(
    block: @WindowDsl Button.() -> Unit
) = applyWindowDsl(Button(), block)
