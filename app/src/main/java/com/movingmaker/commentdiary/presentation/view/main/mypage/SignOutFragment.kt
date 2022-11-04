package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageSignoutBinding
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignOutFragment :
    BaseFragment<FragmentMypageSignoutBinding>(R.layout.fragment_mypage_signout) {
    override val TAG: String = SignOutFragment::class.java.simpleName

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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