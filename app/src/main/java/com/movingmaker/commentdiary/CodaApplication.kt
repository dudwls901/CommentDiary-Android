package com.movingmaker.commentdiary

import android.app.Application
import com.movingmaker.commentdiary.model.AuthProvider

class CodaApplication(): Application() {

    private lateinit var dataStore : AuthProvider

    companion object {
        private lateinit var sampleApplication: CodaApplication
        fun getInstance() : CodaApplication = sampleApplication
    }

    override fun onCreate() {
        super.onCreate()
        sampleApplication = this
        dataStore = AuthProvider(this)
    }

    fun getDataStore() : AuthProvider = dataStore
}