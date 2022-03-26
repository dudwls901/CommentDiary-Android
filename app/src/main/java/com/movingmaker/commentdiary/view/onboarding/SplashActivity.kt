package com.movingmaker.commentdiary.view.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.databinding.ActivitySplashBinding
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.viewmodel.onboarding.IntroViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class SplashActivity: AppCompatActivity(),CoroutineScope {

    private lateinit var binding: ActivitySplashBinding

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

//        window.statusBarColor = getColor(R.color.onboarding_background)


        launch(coroutineContext) {
            var refreshToken = withContext(Dispatchers.IO) {
                CodaApplication.getInstance().getDataStore().refreshToken.first()
            }
            delay(1000L)
            //자동 로그인
            if (refreshToken.isNotEmpty()) {
                //refresh토큰 갱신하는 api 필요 refreshToken만료됐는지 검사 후 accessToken,refreshToken,expiresIn발급
//                finish()
                startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })

            } else {
                startActivity(Intent(this@SplashActivity, OnboardingIntroActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }
}