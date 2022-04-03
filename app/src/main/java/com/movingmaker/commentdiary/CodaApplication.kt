package com.movingmaker.commentdiary

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import com.movingmaker.commentdiary.model.AuthProvider
import com.movingmaker.commentdiary.view.onboarding.OnboardingLoginActivity

class CodaApplication(): Application() {

    private lateinit var dataStore : AuthProvider

//    val database by lazy { CommentDiaryDatabase.getInstance(this) }
//    val repository by lazy { ContactRepository(database!!.contactDao()) }
    companion object {
        private lateinit var codaApplication: CodaApplication
        fun getInstance() : CodaApplication = codaApplication
        const val termsUrl =  "https://glittery-silk-987.notion.site/fb0c6c765a7a411c9362dc8d102c95e0"
        const val policyUrl = "https://www.notion.so/59a704f6702f4382b9398fa3b4a0d780"
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        codaApplication = this
        dataStore = AuthProvider(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if(task.isSuccessful){
                val token = task.result
                Log.d("token", "firebaseToken: ${task.result}")
            }
        }
    }

    fun logOut(){
        getInstance().getDataStore().insertAuth("", "", 0L)
        startActivity(Intent(this, OnboardingLoginActivity::class.java).apply {
            //메인 액티비티 실행하면 현재 화면 필요 없으니 cleartask
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun getDataStore() : AuthProvider = dataStore
}