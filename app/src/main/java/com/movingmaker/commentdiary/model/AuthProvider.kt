package com.movingmaker.commentdiary.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.movingmaker.commentdiary.CodaApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    private suspend fun setAccessToken(token : String){
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }

    private suspend fun setRefreshToken(token : String){
        context.dataStore.edit { preferences ->
            preferences[refreshTokenKey] = token
        }
    }

    private suspend fun setAccessTokenExpiresIn(time : Long){
        context.dataStore.edit { preferences ->
            preferences[accessTokenExpiresInKey] = time
        }
    }

    fun insertAuth(accessToken: String, refreshToken: String, accessTokenExpiresIn: Long){
        CoroutineScope(Dispatchers.IO).launch {
//            CodaApplication.getInstance().getDataStore().setAccessToken("abc")
            CodaApplication.getInstance().getDataStore().setAccessToken(accessToken)
            CodaApplication.getInstance().getDataStore().setRefreshToken(refreshToken)
            CodaApplication.getInstance().getDataStore().setAccessTokenExpiresIn(accessTokenExpiresIn)
        }
    }

}