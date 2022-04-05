package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.commentdiary.databinding.FragmentMypagePushBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PushAlarmOnOffFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = PushAlarmOnOffFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object{
        fun newInstance() : PushAlarmOnOffFragment {
            return PushAlarmOnOffFragment()
        }
    }

    private lateinit var binding: FragmentMypagePushBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypagePushBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        observeDatas()

        return binding.root
    }

    private fun observeDatas(){

        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseLogOut.observe(lifecycleOwner) {
                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                } else {
                    it.errorBody()?.let{ errorBody->
                        RetrofitClient.getErrorResponse(errorBody)?.let{
                            if(it.status==404){

                            }
                            else {
                                CodaSnackBar.make(binding.root, it.message).show()
                            }
                        }
                    }
                }

            }
        }

    }

    private fun initViews() = with(binding){
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myPage")
        }
        pushSwitch.isChecked
        //todo 마이페이지 api에서 뿌려준 pushYN으로
        pushSwitch.setOnCheckedChangeListener { button, state ->
            Log.d(TAG, "initViews: $button $state")
        }
    }

}