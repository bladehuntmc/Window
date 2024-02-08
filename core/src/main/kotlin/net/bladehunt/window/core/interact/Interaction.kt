package net.bladehunt.window.core.interact

import net.bladehunt.window.core.Window
import net.bladehunt.window.core.item.WindowItem

sealed interface Interaction<WindowType : Window<*,*>, NativePlayer> {
    val player: NativePlayer
    val window: WindowType

    sealed interface SlotInteraction {
        val slot: Int
    }
    sealed interface ItemInteraction<NativeItem, NativeMaterial> {
        val item: WindowItem<NativeItem, NativeMaterial>
    }

    data class RightClick<WindowType : Window<*,*>, NativePlayer, NativeItem>(
        override val player: NativePlayer,
        override val window: WindowType,
        override val item: WindowItem<NativeItem,Any>,
        override val slot: Int,
    ) : Interaction<WindowType, NativePlayer>, ItemInteraction<NativeItem, Any>, SlotInteraction

    data class MiddleClick<WindowType : Window<*,*>, NativePlayer, NativeItem>(
        override val player: NativePlayer,
        override val window: WindowType,
        override val item: WindowItem<NativeItem, Any>,
        override val slot: Int,
    ) : Interaction<WindowType, NativePlayer>, ItemInteraction<NativeItem, Any>, SlotInteraction

    data class LeftClick<WindowType : Window<*,*>, NativePlayer, NativeItem>(
        override val player: NativePlayer,
        override val window: WindowType,
        override val item: WindowItem<NativeItem, Any>,
        override val slot: Int,
    ) : Interaction<WindowType, NativePlayer>, ItemInteraction<NativeItem, Any>, SlotInteraction

    data class SneakClick<WindowType : Window<*,*>, NativePlayer, NativeItem>(
        override val player: NativePlayer,
        override val window: WindowType,
        override val item: WindowItem<NativeItem, Any>,
        override val slot: Int,
    ) : Interaction<WindowType, NativePlayer>, ItemInteraction<NativeItem, Any>, SlotInteraction

    data class OuterClick<WindowType : Window<*,*>, NativePlayer>(
        override val player: NativePlayer,
        override val window: WindowType
    ) : Interaction<WindowType, NativePlayer>

    data class ItemSpread<WindowType : Window<*,*>, NativePlayer, NativeItem>(
        override val player: NativePlayer,
        override val window: WindowType,
        override val item: WindowItem<NativeItem, Any>,
        override val slot: Int,
        val even: Boolean
    ) : Interaction<WindowType, NativePlayer>, ItemInteraction<NativeItem, Any>, SlotInteraction
}