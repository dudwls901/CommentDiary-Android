package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageSignoutBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignOutFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = SignOutFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object {
        fun newInstance(): SignOutFragment {
            return SignOutFragment()
        }
    }

    private lateinit var binding: FragmentMypageSignoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageSignoutBinding.inflate(layoutInflater)

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

    private fun observeDatas() {
        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseSignOut.observe(lifecycleOwner) {
                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                    logOut()
                } else {
                    it.errorBody()?.let{ errorBody->
                        RetrofitClient.getErrorResponse(errorBody)?.let {
                            if (it.status == 401) {
                                Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                                    .show()
                                CodaApplication.getInstance().logOut()
                            } else {
                                CodaSnackBar.make(binding.root, "회원 탈퇴에 실패하였습니다.").show()
                            }
                        }
                    }
                }

            }
        }

    }

    private fun initViews() = with(binding) {
        signOutButton.setOnClickListener {
            launch(coroutineContext) {
                binding.loadingBar.isVisible = true
                withContext(Dispatchers.IO) {
                    myPageViewModel.setResponseSignOut()
                }
            }
        }
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myAccount")
        }
    }

    private fun logOut() {
        CodaApplication.getInstance().logOut()
    }

}