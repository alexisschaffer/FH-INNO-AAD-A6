package at.technikum_wien.polzert.news.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences(val feedUrl: String, val showImages : Boolean, val downloadImages : Boolean)

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserPreferencesRepository(private val userPreferencesStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val FEED_URL = stringPreferencesKey(name = "feedUrl")
        val SHOW_IMAGES = booleanPreferencesKey(name = "showImages")
        val DOWNLOAD_IMAGES = booleanPreferencesKey(name = "downloadImages")
    }

    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }
        .map { preferences ->
            val feedUrl = preferences[PreferencesKeys.FEED_URL] ?: "https://www.engadget.com/rss.xml"
            val showImages = preferences[PreferencesKeys.SHOW_IMAGES] ?: true
            val downloadImages = preferences[PreferencesKeys.DOWNLOAD_IMAGES] ?: false
            UserPreferences(feedUrl = feedUrl, showImages = showImages, downloadImages = downloadImages)
        }

    suspend fun updateFeedUrl(feedUrl: String) {
        userPreferencesStore.edit { preferences ->
            preferences[PreferencesKeys.FEED_URL] = feedUrl
        }
    }

    suspend fun updateShowImages(showImages: Boolean) {
        userPreferencesStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_IMAGES] = showImages
        }
    }

    suspend fun updateDownloadImages(downloadImages: Boolean) {
        userPreferencesStore.edit { preferences ->
            preferences[PreferencesKeys.DOWNLOAD_IMAGES] = downloadImages
        }
    }
}
