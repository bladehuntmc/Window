package net.bladehunt.window.core.component

import net.bladehunt.window.core.Parent

interface ParentComponent<Pixel> : Component<Pixel>, Parent<Component<Pixel>>