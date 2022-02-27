package com.movingmaker.commentdiary.view.onboarding

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.movingmaker.commentdiary.BaseFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpBinding
import com.movingmaker.commentdiary.view.OnboardingViewModel
import kotlinx.coroutines.*
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.onboardingviewModel = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        binding.toolbar.setNavigationOnClickListener {
            onboardingViewModel.setCurrentFragment("login")
        }
        initViews()
        observeDatas()

        return binding.root
    }

    private fun observeDatas(){
        binding.lifecycleOwner?.let { lifecycleOwner ->
            onboardingViewModel.responseEmailSend.observe(lifecycleOwner) {
                if (it.isSuccessful) {
                    binding.loadingBar.isVisible = false
                    sendCodeDialog()
                } else {
                    Log.d("ABCABCABCBAC", onboardingViewModel.responseEmailSend.value.toString())
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    Toast.makeText(requireContext(), "이메일 전송 실패", Toast.LENGTH_SHORT).show()
                    binding.loadingBar.isVisible = false
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
            onboardingViewModel.setIsCorrect(isEmailCorrect, "email")
            onboardingViewModel.setCanMakeAccount(
                emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() &&
                        passwordCheckEditText.text.isNotEmpty()
            )
        }
        passwordEditText.addTextChangedListener {
            var hasUpper = false
            var hasLower = false
            var hasNum = false
            var hasSign = false
            for (ch in passwordEditText.text.toString()) {
                when {
                    ch.isUpperCase() -> hasUpper = true
                    ch.isLowerCase() -> hasLower = true
                    ch.isDigit() -> hasNum = true
                    else -> hasSign = true
                }
            }
            val isPasswordCorrect =
                !(passwordEditText.text.isNotEmpty() && (passwordEditText.text.length < 8 || !hasUpper || !hasLower || !hasNum || !hasSign))
            val isPasswordCheckCorrect =
                !(passwordCheckEditText.text.isNotEmpty() && (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            onboardingViewModel.setPassword(passwordEditText.text.toString())
            onboardingViewModel.setIsCorrect(isPasswordCorrect, "password")
            onboardingViewModel.setIsCorrect(isPasswordCheckCorrect, "passwordCheck")
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
            onboardingViewModel.setIsCorrect(isPasswordCheckCorrect, "passwordCheck")
            onboardingViewModel.setCanMakeAccount(
                emailEditText.text.isNotEmpty() &&
                        passwordEditText.text.isNotEmpty() &&
                        passwordCheckEditText.text.isNotEmpty()
            )
        }

        sendAuthButton.setOnClickListener {

            binding.loadingBar.isVisible = true

            launch(coroutineContext) {
                onboardingViewModel.setResponseEmailSend(emailEditText.text.toString())
            }
        }
    }
    private fun sendCodeDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_onboarding_email_code_send, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()

        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val codeEditText = dialogView.findViewById<EditText>(R.id.codeEditText)
        val codeIncorrectTextView = dialogView.findViewById<TextView>(R.id.codeInCorrectTextView)

        binding.lifecycleOwner?.let { lifecycleOwner ->
            onboardingViewModel.responseEmailCodeCheck.observe(lifecycleOwner){
                Log.d(TAG, it.isSuccessful.toString())
                Log.d(TAG, (it.body()?.code ?: 1111).toString())
                Log.d(TAG, it.body()?.message ?: "FAIL")
                if(it.isSuccessful){
                    //인증 완료
                    binding.sendAuthButton.text = getString(R.string.onboarding_complete_auth)
                    binding.sendAuthButton.alpha = 0.4f
                    binding.sendAuthButton.isEnabled = false
                    onboardingViewModel.setEmailCodeCheckComplete(true)
                    alertDialog.dismiss()
                }
                else{
                    codeIncorrectTextView.isVisible = true
                    onboardingViewModel.setEmailCodeCheckComplete(false)
                }
            }
        }

        submitButton.setOnClickListener {
            codeIncorrectTextView.isVisible = false
            if(codeEditText.text.toString().isNotEmpty()) {
                launch(coroutineContext) {
                    onboardingViewModel.setResponseEmailCodeCheck(
                        email = binding.emailEditText.text.toString(),
                        code = codeEditText.text.toString().toInt()
                    )
                }
            }
            else{
                codeIncorrectTextView.isVisible = true
                onboardingViewModel.setEmailCodeCheckComplete(false)
            }
        }


        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}