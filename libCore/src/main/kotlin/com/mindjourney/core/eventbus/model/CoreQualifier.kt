package com.mindjourney.core.eventbus.model

import jakarta.inject.Qualifier

/**
 * Qualifiers for scope of services using event-bus
 */
object CoreQualifier {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Global

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Domain

}
