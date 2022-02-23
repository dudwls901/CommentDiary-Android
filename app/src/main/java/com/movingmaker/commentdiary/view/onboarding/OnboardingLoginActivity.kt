package com.movingmaker.commentdiary.view.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.movingmaker.commentdiary.BaseActivity
import com.movingmaker.commentdiary.BaseFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityOnboardingLoginBinding

class OnboardingLoginActivity: BaseActivity<ActivityOnboardingLoginBinding>(){
    override val TAG: String = OnboardingLoginActivity::class.java.simpleName
    override val layoutRes: Int = R.layout.activity_onboarding_login

    private lateinit var makeAccountResultLauncher: ActivityResultLauncher<Intent>
//Todo login버튼 클릭시 예외 처리, 로그인 처리
//findpassword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeAccountResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val id = result.data?.getStringExtra("id") ?: ""
                val password = result.data?.getStringExtra("password") ?: ""
            }

        }
        initViews()
    }

    private fun initViews() = with(binding){

        makeAccountTextView.setOnClickListener {
            val intent = Intent(this@OnboardingLoginActivity, OnboardingSignUpActivity::class.java)
            makeAccountResultLauncher.launch(intent)
        }
        findPasswordTextView.setOnClickListener {

        }
        loginButton.setOnClickListener {
            showNameInputPopUp()
        }
    }


    private fun showNameInputPopUp() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Fail")
            .setView(editText)
            .setPositiveButton("저장") { _, _ ->
                finish()
//                if (editText.text.isEmpty()) {
//                    showNameInputPopUp()
//                } else {
//                    saveUserName(editText.text.toString())
//                }
            }
            .setCancelable(false)
            .show()
    }

}