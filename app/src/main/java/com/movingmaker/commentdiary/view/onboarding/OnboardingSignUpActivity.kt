package com.movingmaker.commentdiary.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.movingmaker.commentdiary.BaseActivity
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityOnboardingSignUpBinding

class OnboardingSignUpActivity: BaseActivity<ActivityOnboardingSignUpBinding>() {
    override val TAG: String = OnboardingSignUpActivity::class.java.simpleName
    override val layoutRes: Int = R.layout.activity_onboarding_sign_up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
        initViews()
    }

    private fun changeButtonState() =with(binding){
        if(emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() &&
            passwordCheckEditText.text.isNotEmpty() && !emailIncorrectTextView.isVisible &&
            !passwordIncorrectTextView.isVisible && !passwordCheckIncorrectTextView.isVisible
        ){
            signUpButton.alpha = 1.0f
            signUpButton.isEnabled = true
        }
    }

    private fun initViews() = with(binding){

        emailEditText.addTextChangedListener {
            emailIncorrectTextView.isVisible = emailEditText.text.isNotEmpty() &&
                    (emailEditText.text.indexOf('@') ==-1 || emailEditText.text.indexOf('.') ==-1) ||
                    (emailEditText.text.indexOf('@')!=-1 && emailEditText.text.substring(emailEditText.text.indexOf('@')).indexOf('.')==-1)
            Log.d("aaaaaaa",  (emailEditText.text.indexOf('@') ==-1 || emailEditText.text.indexOf('.') ==-1).toString())
            changeButtonState()
        }
        passwordEditText.addTextChangedListener {
            //todo 대소문자 숫자 특수문자 포함 8장 이상
            var hasUpper = false
            var hasLower = false
            var hasNum = false
            var hasSign = false
            for(ch in passwordEditText.text.toString()){
                when{
                    ch.isUpperCase() -> hasUpper =true
                    ch.isLowerCase() -> hasLower = true
                    ch.isDigit() -> hasNum = true
                    else -> hasSign = true
                }
            }
           passwordIncorrectTextView.isVisible = passwordEditText.text.isNotEmpty() &&
                   (passwordEditText.text.length<8 || !hasUpper || !hasLower || !hasNum || !hasSign)
            passwordCheckIncorrectTextView.isVisible = passwordCheckEditText.text.isNotEmpty() &&
                    (passwordEditText.text.toString() != passwordCheckEditText.text.toString())
            changeButtonState()
        }
        passwordCheckEditText.addTextChangedListener {
            passwordCheckIncorrectTextView.isVisible = passwordCheckEditText.text.isNotEmpty() &&
                    (passwordEditText.text.toString() != passwordCheckEditText.text.toString())
            changeButtonState()
        }

        sendAuthButton.setOnClickListener {
            //onboarding
            val intent = Intent(this@OnboardingSignUpActivity, OnboardingIntroActivity::class.java)
            intent.putExtra("result","ok")
            startActivity(intent)
            finish()
        }

        signUpButton.setOnClickListener {
            //인증번호 받기 완료됐으면 콜백
            val intent = Intent(this@OnboardingSignUpActivity, OnboardingLoginActivity::class.java)
            intent.putExtra("id", emailEditText.text.toString())
            intent.putExtra("password", passwordEditText.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}