package com.movingmaker.commentdiary

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.movingmaker.commentdiary.global.MyFirebaseMessagingService
import com.movingmaker.commentdiary.model.AuthProvider
import com.movingmaker.commentdiary.view.onboarding.OnboardingLoginActivity

class CodaApplication : Application() {

    private lateinit var dataStore: AuthProvider
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
        const val termsUrl =
            "https://glittery-silk-987.notion.site/fb0c6c765a7a411c9362dc8d102c95e0"
        const val policyUrl = "https://www.notion.so/59a704f6702f4382b9398fa3b4a0d780"
        lateinit var deviceToken: String

//        fun getCustomExpire() = System.currentTimeMillis()+(60*60*1000)
//        fun getCustomExpire() = System.currentTimeMillis()+60*1000L
        fun getCustomExpire() = System.currentTimeMillis() + 10000L

        const val TAG = "start app CodaApplication"

    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        codaApplication = this
        dataStore = AuthProvider(this)

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

    fun insertAuth(accessToken: String, refreshToken: String, accessTokenExpiresIn: Long) {

        // 자동 로그인 데이터 저장
        sharedPref.edit().apply {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            putLong("accessTokenExpiresIn", accessTokenExpiresIn)
        }.apply()
    }

    fun getAccessToken(): String {
        val accessToken = sharedPref.getString(getString(R.string.accessToken), "Empty Token")
        return accessToken ?: "Empty Token"
    }

    fun getRefreshToken(): String {
        val refreshToken = sharedPref.getString(getString(R.string.refreshToken), "Empty Token")
        return refreshToken ?: "Empty Token"
    }

    fun getAccessExpiresIn(): Long {
        val accessTokenExpiresIn = sharedPref.getLong(getString(R.string.accessTokenExpiresIn), 0)
        return accessTokenExpiresIn
    }

    fun logOut() {

        sharedPref.edit().apply {
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

    fun getDataStore(): AuthProvider = dataStore
}