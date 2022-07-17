package com.movingmaker.commentdiary.view.onboarding

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.databinding.ActivitySplashBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.viewmodel.onboarding.IntroViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivitySplashBinding

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var pushDate: String? = null

    companion object {
        const val REQUEST_CODE_UPDATE = 999
        const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        updateVersion()

        if (intent?.extras != null) {
            for (key: String in intent!!.extras!!.keySet()) {
                val value = intent!!.extras!!.get(key)
                if (value.toString().contains("코멘트가 도착하였습니다.")) {
                    Log.d("push", "$value Key: $key           Value: $value");
                    val yesterday = DateConverter.getCodaToday().minusDays(1)
                    pushDate = DateConverter.ymdFormat(yesterday)
                }
            }
        }
        val loginIntent = Intent(this@SplashActivity, MainActivity::class.java)
        loginIntent.putExtra("pushDate", pushDate)

//        window.statusBarColor = getColor(R.color.onboarding_background)


        launch(coroutineContext) {
            val refreshToken = CodaApplication.getInstance().getRefreshToken()
            delay(1000L)
            //자동 로그인
            if (refreshToken.isNotEmpty()) {
                startActivity(loginIntent)
            } else {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        OnboardingIntroActivity::class.java
                    )
                )
            }
            finish()
        }
    }

    //    //업데이트 확인 + 수락하면 업데이트
    private fun updateVersion() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo


// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.d(TAG, "updateVersion: $appUpdateInfo")
            Log.d(TAG, "updateVersion: ${UpdateAvailability.UPDATE_AVAILABLE}")
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLEΩ
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                Log.d(TAG, " in if updateVersion: ${appUpdateInfo}")
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this@SplashActivity,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    //업데이트 취소시
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("update", "onActivityResult: $requestCode $resultCode $data ")
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                CodaSnackBar.make(binding.root, "업데이트가 취소 되었습니다.").show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}