package net.bladehunt.window.core.item

import net.bladehunt.window.core.platform.NativeHandleContainer

interface WindowMaterial<NativeMaterial> : NativeHandleContainer<NativeMaterial> {
    val id: String
}