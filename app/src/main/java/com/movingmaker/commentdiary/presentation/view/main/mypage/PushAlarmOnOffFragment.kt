package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.CodaSnackBar
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.databinding.FragmentMypagePushBinding
import com.movingmaker.commentdiary.presentation.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PushAlarmOnOffFragment :
    BaseFragment<FragmentMypagePushBinding>(R.layout.fragment_mypage_push) {
    override val TAG: String = PushAlarmOnOffFragment::class.java.simpleName

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.PUSHALARM_ONOFF)
        initViews()
        observeDatas()
    }

    private fun observeDatas() {
        myPageViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        myPageViewModel.snackMessage.observe(viewLifecycleOwner) {
            CodaSnackBar.make(binding.root, it).show()
        }
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