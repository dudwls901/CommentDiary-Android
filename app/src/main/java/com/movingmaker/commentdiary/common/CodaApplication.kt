package com.movingmaker.commentdiary.common

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.util.Constant.KAKAO
import com.movingmaker.commentdiary.presentation.view.onboarding.OnboardingLoginActivity
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CodaApplication : Application() {

    //    private lateinit var dataStore: AuthProvider
    private lateinit var sharedPref: SharedPreferences
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }

    //    val database by lazy { CommentDiaryDatabase.getInstance(this) }
//    val repository by lazy { ContactRepository(database!!.contactDao()) }
    companion object {
        private lateinit var codaApplication: CodaApplication

        @JvmStatic
        fun getInstance(): CodaApplication = codaApplication
        lateinit var deviceToken: String

        //        fun getCustomExpire() = System.currentTimeMillis()+(60*60*1000)
//        fun getCustomExpire() = System.currentTimeMillis()+60*1000L
        fun getCustomExpire() = System.currentTimeMillis() + 10000L

        const val TAG = "start app CodaApplication"

    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        codaApplication = this
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
//        dataStore = AuthProvider(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deviceToken = task.result
            }
        }

        sharedPref = EncryptedSharedPreferences.create(
            this, "enCryptedAuth", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun insertAuth(
        loginType: String,
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresIn: Long
    ) {

        // 자동 로그인 데이터 저장
        sharedPref.edit().apply {
            putString("loginType", loginType)
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            putLong("accessTokenExpiresIn", accessTokenExpiresIn)
        }.apply()
    }

    fun getAccessToken(): String {
        val accessToken = sharedPref.getString(getString(R.string.accessToken), "")
        return accessToken ?: "Empty Token"
    }

    fun getRefreshToken(): String {
        val refreshToken = sharedPref.getString(getString(R.string.refreshToken), "")
        return refreshToken ?: "Empty Token"
    }

    fun getAccessExpiresIn(): Long {
        val accessTokenExpiresIn = sharedPref.getLong(getString(R.string.accessTokenExpiresIn), 0)
        return accessTokenExpiresIn
    }

    fun getLoginType(): String {
        val loginType = sharedPref.getString(getString(R.string.loginType), "")
        return loginType ?: "Empty Type"
    }

    fun signOut() {
        if (getLoginType() == KAKAO) {
            // 연결 끊기
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Timber.e("연결 끊기 실패", error)
                } else {
                    Timber.i("연결 끊기 성공. SDK에서 토큰 삭제 됨")
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

        sharedPref.edit().apply {
            remove(getString(R.string.loginType))
            remove(getString(R.string.accessToken))
            remove(getString(R.string.refreshToken))
            remove(getString(R.string.accessTokenExpiresIn))
        }.apply()

        //todo 화면 두 번 켜지는 이슈 해결하기
        startActivity(Intent(this, OnboardingLoginActivity::class.java).apply {
            //메인 액티비티 실행하면 현재 화면 필요 없으니 cleartask
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

//    fun getDataStore(): AuthProvider = dataStore
}