package com.mindjourney.core.di

import jakarta.inject.Qualifier

object CoreQualifier {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Global

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Domain

}
