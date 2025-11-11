package com.mindjourney.core.tracking.model

data class ScreenChangeCounter(
    val screen: CoreScreen,
    var changeCount: Int = 0
)
