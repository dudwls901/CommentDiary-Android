package com.movingmaker.commentdiary

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import com.movingmaker.presentation.util.PreferencesUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CodaApplication() : Application() {

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                preferencesUtil.insertDeviceToken(task.result)
            }
        }
    }

}