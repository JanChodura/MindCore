package com.mindjourney.core.tracking.model

interface CoreScreen {
    val route: String
    val title: String

    object Unknown : CoreScreen {
        override val route = "unknown"
        override val title = "Unknown"

        override fun toString(): String {
            return "CoreScreen.$title"
        }
    }
}
