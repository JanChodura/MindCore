package com.mindjourney.core.viewmodel

enum class PipelinePhaseEnum {
    NOT_STARTED,
    PREINITIALIZED,
    CONTEXT_INITIALIZED,
    FETCH_IN_PROGRESS,
    FETCH_COMPLETED,
    READY,
    READING_INITIALIZING,
    CONSUMING,
    CONSUMED
}
