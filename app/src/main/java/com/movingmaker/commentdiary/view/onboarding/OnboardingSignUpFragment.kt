package com.movingmaker.commentdiary.view.onboarding

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.NumberFormatException
import kotlin.coroutines.CoroutineContext

class OnboardingSignUpFragment : BaseFragment(),CoroutineScope {
    override val TAG: String = OnboardingSignUpFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels<OnboardingViewModel>()

    private lateinit var binding: FragmentOnboardingSignUpBinding

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        fun newInstance(): OnboardingSignUpFragment {
            return OnboardingSignUpFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.onboardingviewModel = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.backButton.setOnClickListener {
            onboardingViewModel.setCurrentFragment("login")
        }
        initViews()
        observeDatas()

        return binding.root
    }

    private fun observeDatas(){
        binding.lifecycleOwner?.let { lifecycleOwner ->
            onboardingViewModel.responseEmailSend.observe(lifecycleOwner) {
                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                    sendCodeDialog()
                } else {
                    it.errorBody()?.let{ errorBody->
                        RetrofitClient.getErrorResponse(errorBody)?.let{
                            CodaSnackBar.make(binding.root, it.message).show()
                        }
                    }
                }

            }
        }


    }

    private fun initViews() = with(binding) {

        emailEditText.addTextChangedListener {
            val isEmailCorrect = !(emailEditText.text.isNotEmpty() &&
                    (emailEditText.text.indexOf('@') == -1 || emailEditText.text.indexOf('.') == -1) ||
                    (emailEditText.text.indexOf('@') != -1 &&
                            emailEditText.text.substring(emailEditText.text.indexOf('@'))
                                .indexOf('.') == -1))
            if(!isEmailCorrect || emailEditText.text.isEmpty()){
                sendAuthButton.isEnabled = false
                sendAuthButton.alpha = 0.4f
            }
            else{
                sendAuthButton.isEnabled = true
                sendAuthButton.alpha = 1.0f
            }
            onboardingViewModel.setEmail(emailEditText.text.toString())
            onboardingViewModel.setSignUpIsCorrect(isEmailCorrect, "email")
            onboardingViewModel.setCanMakeAccount(
                emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() &&
                        passwordCheckEditText.text.isNotEmpty()
            )
        }
        passwordEditText.addTextChangedListener {
            var hasLetter = false
            var hasNum = false
            var hasSign = false
            for (ch in passwordEditText.text.toString()) {
                when {
                    ch.isLetter() -> hasLetter = true
                    ch.isDigit() -> hasNum = true
                    else -> hasSign = true
                }
            }
            val isPasswordCorrect =
                !(passwordEditText.text.isNotEmpty() && (passwordEditText.text.length < 8 || !hasLetter || !hasNum || !hasSign || passwordEditText.text.length>16))
            val isPasswordCheckCorrect =
                !(passwordCheckEditText.text.isNotEmpty() && (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            onboardingViewModel.setPassword(passwordEditText.text.toString())
            onboardingViewModel.setSignUpIsCorrect(isPasswordCorrect, "password")
            onboardingViewModel.setSignUpIsCorrect(isPasswordCheckCorrect, "passwordCheck")
            onboardingViewModel.setCanMakeAccount(
                emailEditText.text.isNotEmpty() &&
                        passwordEditText.text.isNotEmpty() &&
                        passwordCheckEditText.text.isNotEmpty()
            )
        }

        passwordCheckEditText.addTextChangedListener {
            val isPasswordCheckCorrect = !(passwordCheckEditText.text.isNotEmpty() &&
                    (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            onboardingViewModel.setCheckPassword(passwordCheckEditText.text.toString())
            onboardingViewModel.setSignUpIsCorrect(isPasswordCheckCorrect, "passwordCheck")
            onboardingViewModel.setCanMakeAccount(
                emailEditText.text.isNotEmpty() &&
                        passwordEditText.text.isNotEmpty() &&
                        passwordCheckEditText.text.isNotEmpty()
            )
        }

        sendAuthButton.setOnClickListener {

//            sendCodeDialog()
            launch(coroutineContext) {
                binding.loadingBar.isVisible = true
                withContext(Dispatchers.IO) {
                    onboardingViewModel.setResponseEmailSend(emailEditText.text.toString())
                }
            }
        }
    }
    private fun sendCodeDialog() {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_onboarding_email_code_send)
        dialogView.setCancelable(false)
        dialogView.show()

        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val codeEditText = dialogView.findViewById<EditText>(R.id.codeEditText)
        val codeIncorrectTextView = dialogView.findViewById<TextView>(R.id.codeInCorrectTextView)

        binding.lifecycleOwner?.let { lifecycleOwner ->
            onboardingViewModel.responseIsSuccessCheck.observe(lifecycleOwner){
                if(it.isSuccessful){
                    //인증 완료
                    binding.sendAuthButton.text = getString(R.string.onboarding_complete_auth)
                    binding.sendAuthButton.alpha = 0.4f
                    binding.sendAuthButton.isEnabled = false
                    onboardingViewModel.setEmailCodeCheckComplete(true)
                    dialogView.dismiss()
                }
                else{
                    codeIncorrectTextView.isVisible = true
                    onboardingViewModel.setEmailCodeCheckComplete(false)
                }
            }
        }

        submitButton.setOnClickListener {
            codeIncorrectTextView.isVisible = false
            try {
                val password = codeEditText.text.toString().toInt()
                if(codeEditText.text.toString().isNotEmpty()) {
                    launch(coroutineContext) {
                        withContext(Dispatchers.IO) {
                            onboardingViewModel.setResponseEmailCodeCheck(
                                email = binding.emailEditText.text.toString(),
                                code = password
                            )
                        }
                    }
                }
                else{
                    codeIncorrectTextView.isVisible = true
                    onboardingViewModel.setEmailCodeCheckComplete(false)
                }
            }
            catch (e: NumberFormatException){
                codeIncorrectTextView.isVisible = true
                onboardingViewModel.setEmailCodeCheckComplete(false)
            }

        }


        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }


}