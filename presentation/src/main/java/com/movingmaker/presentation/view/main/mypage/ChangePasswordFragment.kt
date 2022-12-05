package com.movingmaker.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMypageChangePasswordBinding
import com.movingmaker.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentMypageChangePasswordBinding>(R.layout.fragment_mypage_change_password) {

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
        myPageViewModel.password.observe(viewLifecycleOwner) {
            myPageViewModel.validatePassword()
        }

        myPageViewModel.passwordCheck.observe(viewLifecycleOwner) {
            myPageViewModel.validatedPasswordCheck()
        }
    }

    private fun initViews() = with(binding) {
        changePasswordButton.setOnClickListener {
            myPageViewModel.changePassword()
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}