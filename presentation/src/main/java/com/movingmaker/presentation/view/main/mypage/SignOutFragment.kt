package com.movingmaker.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMypageSignoutBinding
import com.movingmaker.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignOutFragment :
    BaseFragment<FragmentMypageSignoutBinding>(R.layout.fragment_mypage_signout) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        initViews()
    }

    private fun initViews() = with(binding) {
        signOutButton.setOnClickListener {
            myPageViewModel.signOut()
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}