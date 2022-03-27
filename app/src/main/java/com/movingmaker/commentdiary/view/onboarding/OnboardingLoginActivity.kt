package com.movingmaker.commentdiary.view.onboarding

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseActivity
import com.movingmaker.commentdiary.databinding.ActivityOnboardingLoginBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OnboardingLoginActivity : BaseActivity<ActivityOnboardingLoginBinding>(), CoroutineScope {
    override val TAG: String = OnboardingLoginActivity::class.java.simpleName
    override val layoutRes: Int = R.layout.activity_onboarding_login

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val onboardingViewModel: OnboardingViewModel by viewModels()

    private lateinit var onboardingLoginFragment: OnboardingLoginFragment
    private lateinit var onboardingSignUpFragment: OnboardingSignUpFragment
    private lateinit var onboardingFindPasswordFragment: OnboardingFindPasswordFragment
    private lateinit var onboardingSignUpSuccessFragment: OnboardingSignUpSuccessFragment
    private var backButtonTime = 0L

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = getColor(R.color.background_ivory)
        //todo 스플래시 대응 수부
//        binding.backgroundLayout.setBackgroundColor(R.color.background_ivory)


        onboardingLoginFragment = OnboardingLoginFragment.newInstance()
        onboardingSignUpFragment = OnboardingSignUpFragment.newInstance()
        onboardingFindPasswordFragment = OnboardingFindPasswordFragment.newInstance()
        onboardingSignUpSuccessFragment = OnboardingSignUpSuccessFragment.newInstance()

        supportFragmentManager.beginTransaction().add(binding.fragmentContainer.id, onboardingLoginFragment)
            .commit()

        binding.lifecycleOwner = this

        initViews()
        observeDatas()
    }

    private fun initViews() = with(binding) {
        addButtonEvent("login")
    }

    private fun observeDatas(){

        onboardingViewModel.responseFindPassword.observe(this) {
            binding.loadingBar.isVisible = false
            sendPasswordDialog(it.isSuccessful)
        }

        onboardingViewModel.responseLogin.observe(this){
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
//                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                val accessToken = it.body()?.result?.accessToken
                val refreshToken = it.body()?.result?.refreshToken
                val accessTokenExpiresIn = it.body()?.result?.accessTokenExpiresIn

                if(accessToken == null || refreshToken == null || accessTokenExpiresIn == null){
//                    Toast.makeText(this, "토큰 저장 실패", Toast.LENGTH_SHORT).show()
                }
                else{
                    CodaApplication.getInstance().getDataStore().insertAuth(accessToken, refreshToken, accessTokenExpiresIn)
                }

                startActivity(Intent(this, MainActivity::class.java).apply {
                    //메인 액티비티 실행하면 현재 화면 필요 없으니 cleartask
                    //메인 액티비티 실행될 때 Signin 종료
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            } else {
                onboardingViewModel.setLoginCorrect(false)
//                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }

        onboardingViewModel.responseSignUpComplete.observe(this){
            val message = it.body()?.message ?: "fail"
            val code = it.body()?.code.toString()
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
//                Toast.makeText(this, "회원가입 성공" + it.body(), Toast.LENGTH_SHORT).show()
                onboardingViewModel.setCurrentFragment("signUpSuccess")

            } else {
//                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }

        val observeFragmentState = Observer<String> { fragment ->
            when (fragment) {
                "login" -> {
                    replaceFragment(onboardingLoginFragment)
                    binding.onboardingBottomButton.alpha = 1.0f
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_login)
                    addButtonEvent(fragment)
                }
                "signUp" -> {
                    replaceFragment(onboardingSignUpFragment)
                    binding.onboardingBottomButton.alpha = 0.4f
                    binding.onboardingBottomButton.isEnabled = false
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_makeaccount)
                    addButtonEvent(fragment)
                    val observeButtonState = Observer<Boolean> { isCorrect ->
                        if (isCorrect) {
                            binding.onboardingBottomButton.alpha = 1.0f
                            binding.onboardingBottomButton.isEnabled = true
                        } else {
                            binding.onboardingBottomButton.alpha = 0.4f
                            binding.onboardingBottomButton.isEnabled = false
                        }
                    }
                    onboardingViewModel.canMakeAccount.observe(this, observeButtonState)
                }
                "findPW" ->{
                    replaceFragment(onboardingFindPasswordFragment)
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_send_password)
                    addButtonEvent(fragment)
                }
                "signUpSuccess" ->{
                    replaceFragment(onboardingSignUpSuccessFragment)
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_button_write_start)
                    addButtonEvent(fragment)
                }
            }
        }

        onboardingViewModel.currentFragment.observe(this, observeFragmentState)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }
    }

    private fun addButtonEvent(fragment: String) {
        when (fragment) {
            "login" -> {
                binding.onboardingBottomButton.setOnClickListener {
                    if(onboardingViewModel.email.value!!.isEmpty() || onboardingViewModel.password.value!!.isEmpty()){
                        onboardingViewModel.setLoginCorrect(false)
                    }
                    else{
                        binding.loadingBar.isVisible = true
                        launch(coroutineContext) {
                            onboardingViewModel.setResponseLogin()
                        }
                    }
                }
            }
            "signUp" -> {
                binding.onboardingBottomButton.setOnClickListener {
                    if(onboardingViewModel.emailCodeCheckComplete.value == true){
                        //회원가입 api 호출
                        launch(coroutineContext) {
                            onboardingViewModel.setResponseSignUp()
                        }
                        return@setOnClickListener
                    }
                    else{
                        CodaSnackBar.make(binding.root,"이메일을 인증해 주세요.").show()
                    }

                }
            }
            "findPW" ->{
                binding.onboardingBottomButton.setOnClickListener {
                    binding.loadingBar.isVisible = true
                    launch(coroutineContext) {
                        onboardingViewModel.setResponseFindPassword()
                    }
                }
            }
            "signUpSuccess" ->{
                binding.onboardingBottomButton.setOnClickListener {
                    binding.loadingBar.isVisible = true
                    launch(coroutineContext) {
                        onboardingViewModel.setResponseLogin()
                    }
                }
            }
        }
    }

    private fun sendPasswordDialog(isSuccess: Boolean) {
        val dialogView = Dialog(this@OnboardingLoginActivity)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_onboarding_password_send)
        dialogView.show()

        val closeButton = dialogView.findViewById<ImageButton>(R.id.closeButton)
        val okButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cannotFoundEmailTextView = dialogView.findViewById<TextView>(R.id.cannotFoundEmailTextView)
        val findPasswordSuccessTextView = dialogView.findViewById<TextView>(R.id.findPasswordSuccessTextView)

        if (isSuccess) {
            closeButton.isVisible = false
            okButton.isVisible = true
            cannotFoundEmailTextView.isVisible = false
            findPasswordSuccessTextView.isVisible = true
        } else {
            closeButton.isVisible = true
            okButton.isVisible = false
            cannotFoundEmailTextView.isVisible = true
            findPasswordSuccessTextView.isVisible = false
        }

        okButton.setOnClickListener {
            dialogView.dismiss()
        }
        closeButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime
        val curFragment = onboardingViewModel.currentFragment.value
        if(curFragment!="signUp" && curFragment != "findPW") {
            if (gapTime in 0..2000) {
                // 2초 안에 두번 뒤로가기 누를 시 앱 종료
                finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            else{
                backButtonTime = currentTime
                CodaSnackBar.make(binding.root, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.").show()
            }
        }
        else {
                onboardingViewModel.setCurrentFragment("login")
        }
    }
}


