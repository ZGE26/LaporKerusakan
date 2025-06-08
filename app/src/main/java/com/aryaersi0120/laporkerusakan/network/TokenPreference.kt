package com.aryaersi0120.laporkerusakan.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TokenPreference {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }
    fun getToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun logout(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}