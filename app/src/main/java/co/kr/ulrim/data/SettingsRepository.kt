package co.kr.ulrim.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserPreferences(
    val fontSize: Int = 1, // 0: Small, 1: Medium, 2: Large
    val isAnimationOn: Boolean = true,
    val isBackgroundOn: Boolean = true,
    val widgetMode: String = "daily", // "daily" or "random"
    val quoteSource: String = "both", // "local_only", "remote_only", "both"
    val widgetQuoteSource: String = "both", // "local_only", "remote_only", "both"
    val onboardingCompleted: Boolean = false
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object Keys {
        val FONT_SIZE = intPreferencesKey("font_size")
        val ANIMATION_ON = booleanPreferencesKey("animation_on")
        val BACKGROUND_ON = booleanPreferencesKey("background_on")
        val TODAY_DATE = androidx.datastore.preferences.core.stringPreferencesKey("today_date")
        val TODAY_QUOTE_ID = androidx.datastore.preferences.core.longPreferencesKey("today_quote_id")
        val WIDGET_MODE = androidx.datastore.preferences.core.stringPreferencesKey("widget_mode")
        val QUOTE_SOURCE = androidx.datastore.preferences.core.stringPreferencesKey("quote_source")
        val WIDGET_QUOTE_SOURCE = androidx.datastore.preferences.core.stringPreferencesKey("widget_quote_source")
        val DEFAULT_QUOTES_VERSION = intPreferencesKey("default_quotes_version")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                fontSize = preferences[Keys.FONT_SIZE] ?: 1,
                isAnimationOn = preferences[Keys.ANIMATION_ON] ?: true,
                isBackgroundOn = preferences[Keys.BACKGROUND_ON] ?: true,
                widgetMode = preferences[Keys.WIDGET_MODE] ?: "daily",
                quoteSource = preferences[Keys.QUOTE_SOURCE] ?: "both",
                widgetQuoteSource = preferences[Keys.WIDGET_QUOTE_SOURCE] ?: "both",
                onboardingCompleted = preferences[Keys.ONBOARDING_COMPLETED] ?: false
            )
        }

    val todayQuoteData: Flow<Pair<String?, Long?>> = dataStore.data
        .map { preferences ->
            Pair(preferences[Keys.TODAY_DATE], preferences[Keys.TODAY_QUOTE_ID])
        }

    suspend fun setFontSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.FONT_SIZE] = size
        }
    }

    suspend fun setAnimationOn(isOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.ANIMATION_ON] = isOn
        }
    }

    suspend fun setBackgroundOn(isOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.BACKGROUND_ON] = isOn
        }
    }

    suspend fun setWidgetMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[Keys.WIDGET_MODE] = mode
        }
    }

    suspend fun setQuoteSource(source: String) {
        dataStore.edit { preferences ->
            preferences[Keys.QUOTE_SOURCE] = source
        }
    }

    suspend fun setWidgetQuoteSource(source: String) {
        dataStore.edit { preferences ->
            preferences[Keys.WIDGET_QUOTE_SOURCE] = source
        }
    }

    suspend fun getDefaultQuotesVersion(): Int {
        return dataStore.data.map { preferences ->
            preferences[Keys.DEFAULT_QUOTES_VERSION] ?: 0
        }.first()
    }

    suspend fun setDefaultQuotesVersion(version: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.DEFAULT_QUOTES_VERSION] = version
        }
    }

    suspend fun setTodayQuote(date: String, quoteId: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.TODAY_DATE] = date
            preferences[Keys.TODAY_QUOTE_ID] = quoteId
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
        }
    }
}
