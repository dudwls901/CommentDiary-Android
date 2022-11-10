package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.databinding.FragmentMypageChangePasswordBinding
import com.movingmaker.commentdiary.presentation.base.BaseFragment
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentMypageChangePasswordBinding>(R.layout.fragment_mypage_change_password) {
    override val TAG: String = ChangePasswordFragment::class.java.simpleName

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        initViews()
        observeDatas()
    }

    private fun observeDatas() {

        myPageViewModel.isPasswordChanged.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    ChangePasswordFragmentDirections.actionChangePasswordFragmentToMyPageFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun initViews() = with(binding) {
        changePasswordButton.setOnClickListener {
            myPageViewModel.changePassword(
                ChangePasswordRequest(
                    password = passwordEditText.text.toString(),
                    checkPassword = passwordCheckEditText.text.toString()
                )
            )
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
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
                !(passwordEditText.text.isNotEmpty() && (passwordEditText.text.length < 8 || !hasLetter || !hasNum || !hasSign || passwordEditText.text.length > 16))
            val isPasswordCheckCorrect =
                !(passwordCheckEditText.text.isNotEmpty() && (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            myPageViewModel.setPasswordCorrect(isPasswordCorrect)
            myPageViewModel.setPasswordCheckCorrect(isPasswordCheckCorrect)
        }
        passwordCheckEditText.addTextChangedListener {
            val isPasswordCheckCorrect = !(passwordCheckEditText.text.isNotEmpty() &&
                    (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            myPageViewModel.setPasswordCheckCorrect(isPasswordCheckCorrect)
        }
    }


}