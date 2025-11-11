package com.mindjourney.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mindjourney.core.util.logging.ILogger
import com.mindjourney.core.util.logging.injectedLogger
import com.mindjourney.core.util.logging.off
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import javax.inject.Inject

/**
 * A repository that uses DataStore to store and retrieve data. It can be used with more specific dataStore repositories.
 *
 * @param dataStore The DataStore instance to use for storing and retrieving data.
 */
open class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val logger: ILogger
) {
    private val log = injectedLogger<DataStoreRepository>(logger, off)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun <T> save(key: Preferences.Key<T>, value: T) = scope.launch {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply { this[key] = value }
        }
    }

    fun <T> read(key: Preferences.Key<T>, default: T): Flow<T> {
        return dataStore.data.map { it[key] ?: default }
    }

    suspend fun isFlagBelowThreshold(
        key: Preferences.Key<Int>,
        threshold: Int,
        default: Int = 0
    ): Boolean = withContext(Dispatchers.IO) {
        log.d("For key: $key is value: ${dataStore.data.first()}")
        dataStore.data.map { it[key] ?: default }.first() < threshold
    }

    fun readTime(key: Preferences.Key<String>, default: String = "00:00"): Flow<LocalTime> {
        return dataStore.data.map { prefs ->
            LocalTime.parse(prefs[key] ?: default)
        }
    }

    fun remove(key: Preferences.Key<*>) = scope.launch {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply { remove(key) }
        }
    }
    open fun close() {
        scope.cancel()
    }
}
