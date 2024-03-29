package com.movingmaker.presentation.view.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseActivity
import com.movingmaker.presentation.databinding.ActivitySplashBinding
import com.movingmaker.presentation.util.PreferencesUtil
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymdFormat
import com.movingmaker.presentation.view.main.MainActivity
import com.movingmaker.presentation.view.snackbar.CodaSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private var pushDate: String? = null

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    companion object {
        const val REQUEST_CODE_UPDATE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        lifecycleScope.launch {
            updateVersion()

            delay(1000L)
            //자동 로그인
            login()
        }
    }

    private fun login() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            startActivity(getLoginIntent())
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

    private fun getLoginIntent(): Intent {
        if (intent?.extras != null) {
            for (key: String in intent!!.extras!!.keySet()) {
                val value = intent!!.extras!!.get(key)
                if (value.toString().contains("코멘트가 도착하였습니다.")) {
                    Timber.d("$value Key: $key           Value: $value")
                    val yesterday = getCodaToday().minusDays(1)
                    pushDate = ymdFormat(yesterday)
                }
            }
        }
        return Intent(this@SplashActivity, MainActivity::class.java).apply {
            putExtra("pushDate", pushDate)
        }
    }

    //업데이트 확인 + 수락하면 업데이트
    private fun updateVersion() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo


        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Timber.d("updateVersion: $appUpdateInfo")
            Timber.d("updateVersion: ${UpdateAvailability.UPDATE_AVAILABLE}")
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLEΩ
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                Timber.d(" in if updateVersion: ${appUpdateInfo}")
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
        Timber.d("onActivityResult: $requestCode $resultCode $data ")
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                CodaSnackBar.make(binding.root, "업데이트가 취소 되었습니다.").show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}