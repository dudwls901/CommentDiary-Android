package com.movingmaker.presentation.util

import android.content.SharedPreferences
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber
import javax.inject.Inject

class PreferencesUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun insertAuth(
        loginType: String,
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresIn: Long,
        userId: Long? = null, // 토큰 재발급의 경우 Null
    ) {
        // 자동 로그인 데이터 저장
        sharedPreferences.edit().apply {
            putString(LOGIN_TYPE, loginType)
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            putLong(ACCESS_TOKEN_EXPIRES_IN, accessTokenExpiresIn)
            putLong(USER_ID, userId ?: getUserId())
        }.apply()
    }

    fun insertDeviceToken(
        deviceToken: String,
    ) {
        // 자동 로그인 데이터 저장
        sharedPreferences.edit().putString(DEVICE_TOKEN, deviceToken).apply()
    }

    fun getDeviceToken(): String {
        val deviceToken = sharedPreferences.getString(DEVICE_TOKEN, EMPTY_TOKEN)
        return deviceToken ?: EMPTY_TOKEN
    }

    fun getAccessToken(): String {
        val accessToken =
            sharedPreferences.getString(ACCESS_TOKEN, EMPTY_TOKEN)
        return accessToken ?: EMPTY_TOKEN
    }

    fun getRefreshToken(): String {
        val refreshToken =
            sharedPreferences.getString(REFRESH_TOKEN, EMPTY_TOKEN)
        return refreshToken ?: EMPTY_TOKEN
    }

    fun getAccessExpiresIn(): Long {
        return sharedPreferences.getLong(
            ACCESS_TOKEN_EXPIRES_IN,
            0
        )
    }

    fun getLoginType(): String {
        val loginType =
            sharedPreferences.getString(LOGIN_TYPE, EMPTY_TYPE)
        return loginType ?: EMPTY_TYPE
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(
            USER_ID, EMPTY_USER
        )
    }

    fun signOut() {
        if (getLoginType() == KAKAO) {
            // 연결 끊기
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Timber.e("연결 끊기 실패", error)
                } else {
                    Timber.i("연결 끊기 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
        logOut()
    }

    fun logOut() {
        if (getLoginType() == KAKAO) {
            // 카카오 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Timber.e("로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Timber.i("로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
        insertAuth(EMPTY_TOKEN, EMPTY_TOKEN, EMPTY_TOKEN, 1L, EMPTY_USER)
    }

}