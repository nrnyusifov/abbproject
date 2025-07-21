package com.example.abbproject.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
        val EMAIL_KEY = stringPreferencesKey("email")
    }

    val rememberMeFlow: Flow<Boolean> = context.dataStore.data
        .map { it[REMEMBER_ME_KEY] ?: false }

    val savedEmailFlow: Flow<String> = context.dataStore.data
        .map { it[EMAIL_KEY] ?: "" }

    suspend fun saveCredentials(email: String, rememberMe: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[REMEMBER_ME_KEY] = rememberMe
            prefs[EMAIL_KEY] = if (rememberMe) email else ""
        }
    }

}
