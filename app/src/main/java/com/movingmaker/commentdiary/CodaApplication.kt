package com.movingmaker.commentdiary

import android.app.Application
import android.content.Intent
import com.movingmaker.commentdiary.model.AuthProvider
import com.movingmaker.commentdiary.view.onboarding.OnboardingLoginActivity

class CodaApplication(): Application() {

    private lateinit var dataStore : AuthProvider

    companion object {
        private lateinit var codaApplication: CodaApplication
        fun getInstance() : CodaApplication = codaApplication
    }

    override fun onCreate() {
        super.onCreate()
        codaApplication = this
        dataStore = AuthProvider(this)
    }

    fun logOut(){
        startActivity(Intent(this, OnboardingLoginActivity::class.java).apply {
            //메인 액티비티 실행하면 현재 화면 필요 없으니 cleartask
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun getDataStore() : AuthProvider = dataStore
}