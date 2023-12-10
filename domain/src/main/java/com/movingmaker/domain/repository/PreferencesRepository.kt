package com.movingmaker.domain.repository

import kotlinx.coroutines.flow.Flow


interface PreferencesRepository {

    fun getIsTermsAccept(): Flow<Boolean>

    suspend fun setIsTermsAccept(isTermsAccept: Boolean)

    fun getIsPolicyAccept(): Flow<Boolean>

    suspend fun setIsPolicyAccept(isPolicyAccept: Boolean)

    fun getIsPushAccept(): Flow<Boolean>

    suspend fun setIsPushAccept(isPushAccept: Boolean)
}