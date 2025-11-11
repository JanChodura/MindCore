package com.mindjourney.core.observer.trigger.model

data class PollConfig (val cycles: Int, val intervalSec: Int){
    companion object {
        val DEFAULT = PollConfig(cycles = 5, intervalSec = 60)
    }
}
