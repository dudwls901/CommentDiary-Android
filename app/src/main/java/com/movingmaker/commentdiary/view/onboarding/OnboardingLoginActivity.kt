package com.movingmaker.commentdiary.view.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.movingmaker.commentdiary.BaseActivity
import com.movingmaker.commentdiary.BaseFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityOnboardingLoginBinding
import com.movingmaker.commentdiary.model.remote.request.SignUpRequest
import com.movingmaker.commentdiary.view.OnboardingViewModel
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

    // todo indpassword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        onboardingViewModel.responseSignUpComplete.observe(this){
            Log.d(TAG, it.isSuccessful.toString())
            Log.d(TAG, it.body()?.code.toString())
            Log.d(TAG, it.body()?.message ?: "FAIL")
            val message = it.body()?.message ?: "fail"
            val code = it.body()?.code.toString()

            if (it.isSuccessful) {
                Log.d("성공",message + " " + code)
                Toast.makeText(this, "회원가입 성공" + it.body(), Toast.LENGTH_SHORT).show()
                //로그인 고고
                onboardingViewModel.setCurrentFragment("signUpSuccess")

            } else {
                Log.d("실패",message + " " + code)
                Toast.makeText(this, "회원가입실패", Toast.LENGTH_SHORT).show()
            }
        }

        val observeFragmentState = Observer<String> { fragment ->
            when (fragment) {
                "login" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, onboardingLoginFragment)
                        .commit()
                    binding.onboardingBottomButton.alpha = 1.0f
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_login)
                    addButtonEvent(fragment)
                }
                "signUp" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, onboardingSignUpFragment)
                        .commit()
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
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, onboardingFindPasswordFragment)
                        .commit()
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_send_password)
                    addButtonEvent(fragment)
                }
                "signUpSuccess" ->{
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, onboardingSignUpSuccessFragment)
                        .commit()
                    binding.onboardingBottomButton.isEnabled = true
                    binding.onboardingBottomButton.text = getString(R.string.onboarding_button_write_start)
                    addButtonEvent(fragment)
                }
            }
        }


        onboardingViewModel.currentFragment.observe(this, observeFragmentState)
    }

    private fun addButtonEvent(fragment: String) {
        when (fragment) {
            "login" -> {
                binding.onboardingBottomButton.setOnClickListener {
                    //Todo login버튼 클릭시 예외 처리, 로그인 처리
                    Toast.makeText(this@OnboardingLoginActivity, "login", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, "이메일 인증 먼저 하세요", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            "findPW" ->{
                //todo 비밀번호 전송하기 retrofit, 팝
                binding.onboardingBottomButton.setOnClickListener {
                    Toast.makeText(this, "비밀번호 전송하기", Toast.LENGTH_SHORT).show()
                    sendPasswordDialog()
                }
            }
            "signUpSuccess" ->{
                binding.onboardingBottomButton.setOnClickListener {
                    Toast.makeText(this, "로그인하기", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendPasswordDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_onboarding_password_send, null)
        val builder = android.app.AlertDialog.Builder(this)
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()

        val ok_btn = dialogView.findViewById<Button>(R.id.submitButton)
        ok_btn.setOnClickListener {
            Toast.makeText(
                this,
                "OK 버튼을 눌렀습니다.",
                Toast.LENGTH_LONG
            ).show()
            alertDialog.dismiss()

        }
        val closeButton = dialogView.findViewById<ImageButton>(R.id.closeButton)
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}


