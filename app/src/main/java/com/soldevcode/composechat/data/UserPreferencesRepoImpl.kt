package com.soldevcode.composechat.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.soldevcode.composechat.Languages
import com.soldevcode.composechat.UserPreferences
import com.soldevcode.composechat.util.Constants.DATA_STORE_FILE_NAME
import com.soldevcode.composechat.util.LanguageItems
import com.soldevcode.composechat.util.TextToSpeechItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

interface UserPreferencesRepo {
    fun readUserPreferences(context: Context): Flow<UserPreferences>
    suspend fun updateUserPreferences(context: Context, languages: Languages)
}

private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer
)

class UserPreferencesRepoImpl : UserPreferencesRepo {

    private val TAG: String = "UserPreferencesRepo"

    override fun readUserPreferences(context: Context) = context.userPreferencesStore.data
        .catch { exception ->
            when (exception) {
                is IOException -> {
                    Log.e(TAG, "Error reading sort order preferences.", exception)
                    emit(UserPreferences.getDefaultInstance())
                }
                else -> throw exception
            }
        }

    override suspend fun updateUserPreferences(
        context: Context,
        languages: Languages
    ) {
        val selectedLanguage = LanguageItems(
            languages = "en", // Set the desired language here
            textLanCode = TextToSpeechItems("en-US", "en-GB"),
            recordingLanCode = "en-IN"
        )
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setSelectedLanguage(languages).build()
        }
    }
}