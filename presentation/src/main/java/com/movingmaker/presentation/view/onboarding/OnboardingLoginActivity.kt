package com.movingmaker.presentation.view.onboarding

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseActivity
import com.movingmaker.presentation.databinding.ActivityOnboardingLoginBinding
import com.movingmaker.presentation.view.snackbar.CodaSnackBar
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingLoginActivity :
    BaseActivity<ActivityOnboardingLoginBinding>(R.layout.activity_onboarding_login) {

    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private var backButtonTime = 0L
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.background_ivory)
        binding.vm = onboardingViewModel
        binding.activity = this

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.onboardingNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime

        if (navController.currentDestination!!.id == R.id.onboardingLoginFragment) {
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


