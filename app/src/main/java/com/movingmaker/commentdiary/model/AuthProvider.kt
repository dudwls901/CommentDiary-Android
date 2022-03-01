package com.movingmaker.commentdiary.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class AuthProvider(private val context : Context) {

    private val Context.dataStore  by preferencesDataStore(name = "dataStore")

    private val accessTokenKey = stringPreferencesKey("accessTokenKey")
    private val refreshTokenKey = stringPreferencesKey("refreshTokenKey")
    private val accessTokenExpiresInKey = longPreferencesKey("accessTokenExpiresInKey")

    val accessToken : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[accessTokenKey] ?: ""
        }

    val refreshToken : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[refreshTokenKey] ?: ""
        }

    val accessTokenExpiresIn : Flow<Long> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[accessTokenExpiresInKey] ?: 0L
        }

    suspend fun setAccessToken(token : String){
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }

    suspend fun setRefreshToken(token : String){
        context.dataStore.edit { preferences ->
            preferences[refreshTokenKey] = token
        }
    }

    suspend fun setAccessTokenExpiresIn(time : Long){
        context.dataStore.edit { preferences ->
            preferences[accessTokenExpiresInKey] = time
        }
    }

}