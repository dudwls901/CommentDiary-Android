package com.movingmaker.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMypagePushBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PushAlarmOnOffFragment :
    BaseFragment<FragmentMypagePushBinding>(R.layout.fragment_mypage_push) {

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.PUSHALARM_ONOFF)
        initViews()
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
//            fragmentViewModel.setFragmentState("myPage")
            findNavController().popBackStack()
        }
        pushSwitch.isChecked
        pushSwitch.setOnCheckedChangeListener { button, state ->
            Timber.d("initViews: $button $state")
        }
        pushSwitch.setOnClickListener {
            myPageViewModel.setResponsePatchCommentPushState()
        }
    }

}