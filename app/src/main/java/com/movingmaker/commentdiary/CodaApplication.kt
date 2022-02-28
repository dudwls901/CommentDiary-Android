package com.movingmaker.commentdiary

import android.app.Application
import com.movingmaker.commentdiary.model.AuthProvider

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

    fun getDataStore() : AuthProvider = dataStore
}