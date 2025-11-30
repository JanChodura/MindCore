package com.mindjourney.core.viewmodel.helper

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
