package com.movingmaker.commentdiary

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.movingmaker.presentation.util.PreferencesUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CodaApplication() : Application() {

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    companion object {

        //        fun getCustomExpire() = System.currentTimeMillis()+(60*60*1000)
//        fun getCustomExpire() = System.currentTimeMillis()+60*1000L
        fun getCustomExpire() = System.currentTimeMillis() + 1000L

    }

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                preferencesUtil.insertDeviceToken(task.result)
            }
        }
    }

}