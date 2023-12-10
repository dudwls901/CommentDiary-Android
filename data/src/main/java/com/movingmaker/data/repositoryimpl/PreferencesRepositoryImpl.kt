package com.movingmaker.data.repositoryimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.movingmaker.data.util.KEY_POLICY_ACCEPT
import com.movingmaker.data.util.KEY_PUSH_ACCEPT
import com.movingmaker.data.util.KEY_TERMS_ACCEPT
import com.movingmaker.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {

    override fun getIsTermsAccept(): Flow<Boolean> = dataStore.data.map {
        it[KEY_TERMS_ACCEPT] ?: false
    }

    override suspend fun setIsTermsAccept(isTermsAccept: Boolean) {
        dataStore.edit {
            it[KEY_TERMS_ACCEPT] = isTermsAccept
        }
    }

    override fun getIsPolicyAccept(): Flow<Boolean> = dataStore.data.map {
        it[KEY_POLICY_ACCEPT] ?: false
    }

    override suspend fun setIsPolicyAccept(isPolicyAccept: Boolean) {
        dataStore.edit {
            it[KEY_POLICY_ACCEPT] = isPolicyAccept
        }
    }

    override fun getIsPushAccept(): Flow<Boolean> = dataStore.data.map {
        it[KEY_PUSH_ACCEPT] ?: false
    }

    override suspend fun setIsPushAccept(isPushAccept: Boolean) {
        dataStore.edit {
            it[KEY_PUSH_ACCEPT] = isPushAccept
        }
    }
}