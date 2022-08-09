package com.movingmaker.commentdiary.view.onboarding

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseActivity
import com.movingmaker.commentdiary.databinding.ActivityOnboardingLoginBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.util.setOnSingleClickListener
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext

class OnboardingLoginActivity : BaseActivity<ActivityOnboardingLoginBinding>(R.layout.activity_onboarding_login), CoroutineScope {
    override val TAG: String = OnboardingLoginActivity::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private var backButtonTime = 0L
    private lateinit var navController: NavController

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = getColor(R.color.background_ivory)
        //todo 스플래시 대응 수부
//        binding.backgroundLayout.setBackgroundColor(R.color.background_ivory)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.onboardingNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.vm = onboardingViewModel
        initViews()
        observeDatas()
    }

    private fun initViews() = with(binding) {
        makeAccountTextView.setOnClickListener {
            //화면 넘어갈 시 공용 변수인 email, password 초기화
            onboardingViewModel.setEmail("")
            onboardingViewModel.setPassword("")
            navController.navigate(OnboardingLoginFragmentDirections.actionOnboardingLoginFragmentToOnboardingSignUpTermsFragment())
        }
        findPasswordTextView.setOnClickListener {
            navController.navigate(OnboardingLoginFragmentDirections.actionOnboardingLoginFragmentToOnboardingFindPasswordFragment())
        }
    }

    private fun observeDatas() {
        onboardingViewModel.successFindPassword.observe(this) {
            if (it) {
                binding.onboardingBottomButton.text = getString(R.string.onboarding_go_to_login)
            }
        }

        onboardingViewModel.currentFragment.observe(this) { fragment ->
            binding.onboardingBottomButton.alpha = 1f
            binding.onboardingBottomButton.isEnabled = true
            when (fragment) {
                FRAGMENT_NAME.LOGIN_BEFORE -> {
                    binding.onboardingBottomButton.visibility = View.GONE
                }
                FRAGMENT_NAME.LOGIN -> {
                    //로그인 화면으로 되돌아올 때 email, password 초기화
                    onboardingViewModel.setEmail("")
                    onboardingViewModel.setPassword("")
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        login()
                    }
                }
                FRAGMENT_NAME.KAKAO_TERMS -> {
                    onboardingViewModel.isPolicyAccept.observe(this) {
                        setTermsButtonState(it && onboardingViewModel.isTermsAccept.value!!)
                    }
                    onboardingViewModel.isTermsAccept.observe(this) {
                        setTermsButtonState(it && onboardingViewModel.isTermsAccept.value!!)
                    }
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        kakaoSignUp()
                    }
                }
                FRAGMENT_NAME.SIGNUP_TERMS -> {
                    onboardingViewModel.isPolicyAccept.observe(this) {
                        setTermsButtonState(it && onboardingViewModel.isTermsAccept.value!!)
                    }
                    onboardingViewModel.isTermsAccept.observe(this) {
                        setTermsButtonState(it && onboardingViewModel.isTermsAccept.value!!)
                    }
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        navController.navigate(OnboardingSignUpTermsFragmentDirections.actionOnboardingSignUpTermsFragmentToOnboardingSignUpEmailFragment())
                    }
                }
                FRAGMENT_NAME.SIGNUP_EMAIL -> {
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        sendCode()
                    }
                }
                FRAGMENT_NAME.SIGNUP_CODE -> {
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        checkCode()
                    }
                }
                FRAGMENT_NAME.SIGNUP_PASSWORD -> {
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        signUp()
                    }
                }
                FRAGMENT_NAME.SIGNUP_SUCCESS -> {
//                    binding.onboardingBottomButton.setOnClickListener {
//                        binding.loadingBar.isVisible = true
//                        launch(coroutineContext) {
//                            withContext(Dispatchers.IO) {
//                                onboardingViewModel.setResponseLogin()
//                            }
//                        }
//                    }
                }
                FRAGMENT_NAME.FIND_PASSWORD -> {
                    binding.onboardingBottomButton.setOnSingleClickListener {
                        findPassword()
                    }
                }
            }
        }
    }

    private fun setTermsButtonState(canNext: Boolean) {
        if (canNext) {
            binding.onboardingBottomButton.alpha = 1f
            binding.onboardingBottomButton.isEnabled = true
        } else {
            binding.onboardingBottomButton.alpha = 0.4f
            binding.onboardingBottomButton.isEnabled = false
        }
    }

    private fun findPassword() {
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.findPassword()) {
                binding.onboardingBottomButton.setOnSingleClickListener {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun login() {
        onboardingViewModel.validateEmail("email")
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.login()) {
                startActivity(Intent(this@OnboardingLoginActivity, MainActivity::class.java).apply {
                    finish()
                })
            }
        }
    }

    private fun kakaoSignUp() {
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.kakaoSignUpSetAccepts()) {
                startActivity(
                    Intent(
                        this@OnboardingLoginActivity,
                        MainActivity::class.java
                    ).apply {
                        finish()
                    })
            }
        }

    }

    private fun signUp() {
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.signUp()) {
                login()
            }
        }
    }

    private fun checkCode() {
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.checkCode()) {
                navController.navigate(OnboardingSignUpCodeFragmentDirections.actionOnboardingSignUpCodeFragmentToOnboardingSignUpPasswordFragment())
            }
        }
    }

    private fun sendCode() {
        launch {
            onboardingViewModel.onLoading()
            if (onboardingViewModel.emailCodeSend()) {
                navController.navigate(OnboardingSignUpEmailFragmentDirections.actionOnboardingSignUpEmailFragmentToOnboardingSignUpCodeFragment())
            }
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime

        if (navController.currentDestination!!.id == R.id.onboardingLoginBeforeFragment
        ) {
            if (gapTime in 0..2000) {
                // 2초 안에 두번 뒤로가기 누를 시 앱 종료
                finish()
            } else {
                backButtonTime = currentTime
                CodaSnackBar.make(binding.root, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.").show()
            }
        } else {
            super.onBackPressed()
        }
    }
}


