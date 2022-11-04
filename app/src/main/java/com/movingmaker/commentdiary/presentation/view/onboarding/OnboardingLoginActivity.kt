package com.movingmaker.commentdiary.presentation.view.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.CodaSnackBar
import com.movingmaker.commentdiary.common.base.BaseActivity
import com.movingmaker.commentdiary.common.util.EventObserver
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.common.util.setOnSingleClickListener
import com.movingmaker.commentdiary.databinding.ActivityOnboardingLoginBinding
import com.movingmaker.commentdiary.presentation.view.main.MainActivity
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingLoginActivity :
    BaseActivity<ActivityOnboardingLoginBinding>(R.layout.activity_onboarding_login) {
    //class OnboardingLoginActivity: AppCompatActivity(), CoroutineScope{
    override val TAG: String = OnboardingLoginActivity::class.java.simpleName

    //    lateinit var binding: ActivityOnboardingLoginBinding
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private var backButtonTime = 0L
    private lateinit var navController: NavController

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.background_ivory)
        binding.vm = onboardingViewModel
        binding.activity = this
        //todo 스플래시 대응 수부
//        binding.backgroundLayout.setBackgroundColor(R.color.background_ivory)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.onboardingNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
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

        onboardingViewModel.toastMessage.observe(this,EventObserver{
            CodaSnackBar.make(binding.root, it).show()
        })

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
        lifecycleScope.launch {
            if (onboardingViewModel.findPassword()) {
                binding.onboardingBottomButton.setOnSingleClickListener {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun login() {
        onboardingViewModel.validateEmail("email")
        lifecycleScope.launch {
            if (onboardingViewModel.login()) {
                startActivity(Intent(this@OnboardingLoginActivity, MainActivity::class.java).apply {
                    finish()
                })
            }
        }
    }

    private fun kakaoSignUp() {
        lifecycleScope.launch {
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
        lifecycleScope.launch {
            if (onboardingViewModel.signUp()) {
                login()
            }
        }
    }

    private fun checkCode() {
        lifecycleScope.launch {
            if (onboardingViewModel.checkCode()) {
                navController.navigate(OnboardingSignUpCodeFragmentDirections.actionOnboardingSignUpCodeFragmentToOnboardingSignUpPasswordFragment())
            }
        }
    }

    private fun sendCode() {
        lifecycleScope.launch {
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


