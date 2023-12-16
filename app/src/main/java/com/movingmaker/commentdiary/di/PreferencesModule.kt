package com.movingmaker.commentdiary.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    private const val USER_PREFERENCES = "USER_PREFERENCES"

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(USER_PREFERENCES)
        }
    }

    @Provides
    @Singleton
    fun provideMasterKey(@ApplicationContext context: Context) =
        MasterKey
            .Builder(
                context,
                MasterKey.DEFAULT_MASTER_KEY_ALIAS
            )
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
        masterKey: MasterKey
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "encryptedAuth",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}