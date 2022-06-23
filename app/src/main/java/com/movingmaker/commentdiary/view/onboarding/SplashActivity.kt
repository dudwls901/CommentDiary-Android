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
    private val introViewModel: IntroViewModel by viewModels()
    private var pushDate: String? = null

    companion object{
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
                if(value.toString().contains("코멘트가 도착하였습니다.")) {
                    Log.d("pushaaaaaa", "$value Key: " + key + "           Value: " + value);
                    val yesterDay = DateConverter.getCodaToday().minusDays(1)
                    pushDate = DateConverter.ymdFormat(yesterDay)
                }
            }
        }
        val loginIntent = Intent(this@SplashActivity, MainActivity::class.java)
        loginIntent.putExtra("pushDate", pushDate)

//        window.statusBarColor = getColor(R.color.onboarding_background)


        launch(coroutineContext) {
            val refreshToken = CodaApplication.getInstance().getRefreshToken()
            //앞선 로그인을 통해 발급 받은 토큰이 있는지 확인
            //JWT토큰 사용하므로 필요 없음
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "토큰 정보 보기 실패", error)
                        Toast.makeText(this@SplashActivity, "login please", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        UserApiClient.instance.me { user, error ->
                            Toast.makeText(this@SplashActivity, "$user $error", Toast.LENGTH_SHORT)
                                .show()
                            if (error != null) {
                                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
                            } else if (user != null) {

                                Log.i(
                                    ContentValues.TAG, "사용자 정보 요청 성공" +
                                            "\n정보 : ${user.properties}" +
                                            "\n회원번호: ${user.id}" +
                                            "\n이메일: ${user.kakaoAccount?.email}" +
                                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                                )

//                                startActivity(loginIntent.apply {
//                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                })
                            }

                        }
                    }
                }
            }
//            var refreshToken = withContext(Dispatchers.IO) {
//                CodaApplication.getInstance().getDataStore().refreshToken.first()
//            }
            delay(1000L)
            //자동 로그인
            if (refreshToken.isNotEmpty()) {
                //refresh토큰 갱신하는 api 필요 refreshToken만료됐는지 검사 후 accessToken,refreshToken,expiresIn발급
//                finish()
                startActivity(loginIntent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })

            } else {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        OnboardingIntroActivity::class.java
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
            }
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
        Log.d("updateeeeeee", "onActivityResult: $requestCode $resultCode $data ")
        if(requestCode== REQUEST_CODE_UPDATE){
            if(resultCode != Activity.RESULT_OK){
                CodaSnackBar.make(binding.root, "업데이트가 취소 되었습니다.").show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}