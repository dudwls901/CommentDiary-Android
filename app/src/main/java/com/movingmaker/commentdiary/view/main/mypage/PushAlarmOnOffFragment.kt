package com.movingmaker.commentdiary.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypagePushBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PushAlarmOnOffFragment: BaseFragment<FragmentMypagePushBinding>(R.layout.fragment_mypage_push), CoroutineScope {
    override val TAG: String = PushAlarmOnOffFragment::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.PUSHALARM_ONOFF)
        initViews()
        observeDatas()
    }

    private fun observeDatas(){
        myPageViewModel.errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        myPageViewModel.snackMessage.observe(viewLifecycleOwner){
            CodaSnackBar.make(binding.root, it ).show()
        }
    }

    private fun initViews() = with(binding){
        backButton.setOnClickListener {
//            fragmentViewModel.setFragmentState("myPage")
            findNavController().popBackStack()
        }
        pushSwitch.isChecked
        pushSwitch.setOnCheckedChangeListener { button, state ->
            Log.d(TAG, "initViews: $button $state")
        }
        pushSwitch.setOnClickListener {
            myPageViewModel.setResponsePatchCommentPushState()
        }
    }

}