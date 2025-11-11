package com.mindjourney.core.util.logging

val off = LogDebugSwitch.OFF
val on = LogDebugSwitch.ON
val permanent = LogDebugSwitch.PERMANENT

enum class LogDebugSwitch {
    ON,
    OFF,
    PERMANENT;

    fun isLive(): Boolean {
        return this == PERMANENT || this == ON
    }
}
