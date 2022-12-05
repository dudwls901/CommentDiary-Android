package com.movingmaker.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment :
    BaseFragment<FragmentMypageMyaccountBinding>(R.layout.fragment_mypage_myaccount) {

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.MY_ACCOUNT)
        binding.vm = myPageViewModel
        initViews()
    }

    private fun initViews() = with(binding) {
        logoutLayout.setOnClickListener {
            myPageViewModel.logout()
        }
        signOutLayout.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToSignOutFragment()
            findNavController().navigate(action)
        }
        changePasswordLayout.setOnClickListener {
            val action =
                MyAccountFragmentDirections.actionMyAccountFragmentToChangePasswordFragment()
            findNavController().navigate(action)
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}