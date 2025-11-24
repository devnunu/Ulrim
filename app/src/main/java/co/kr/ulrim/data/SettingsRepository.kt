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
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserPreferences(
    val fontSize: Int = 1, // 0: Small, 1: Medium, 2: Large
    val isAnimationOn: Boolean = true,
    val isBackgroundOn: Boolean = true
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
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                fontSize = preferences[Keys.FONT_SIZE] ?: 1,
                isAnimationOn = preferences[Keys.ANIMATION_ON] ?: true,
                isBackgroundOn = preferences[Keys.BACKGROUND_ON] ?: true
            )
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
}
