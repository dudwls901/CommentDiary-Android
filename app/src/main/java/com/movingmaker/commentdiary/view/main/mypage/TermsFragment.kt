package com.movingmaker.commentdiary.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageTermsBinding
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TermsFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = TermsFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object{
        fun newInstance() : TermsFragment {
            return TermsFragment()
        }
    }

    private lateinit var binding: FragmentMypageTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageTermsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        observeDatas()

        return binding.root
    }

    private fun observeDatas(){
//        binding.lifecycleOwner?.let { lifecycleOwner ->
//            myPageViewModel.responseSignOut.observe(lifecycleOwner) {
////                binding.loadingBar.isVisible = false
//                if (it.isSuccessful) {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
//                    logOut()
//                } else {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
////                    Toast.makeText(requireContext(), "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }

//        binding.lifecycleOwner?.let { lifecycleOwner ->
//            myPageViewModel.responseLogOut.observe(lifecycleOwner) {
////                binding.loadingBar.isVisible = false
//                if (it.isSuccessful) {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
////                    logOut()
//                } else {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
////                    Toast.makeText(requireContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }



    }

    private fun initViews() = with(binding){
//        signOutButton.setOnClickListener {
//            launch(coroutineContext) {
//                myPageViewModel.setResponseSignOut()
//            }
//        }
//        logoutLayout.setOnClickListener {
//            launch(coroutineContext) {
//                myPageViewModel.setResponseLogOut()
//            }
//        }
//        signOutLayout.setOnClickListener {
//            fragmentViewModel.setFragmentState("signOut")
//        }
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myPage")
        }
//        changePasswordButton.setOnClickListener {
//            launch(coroutineContext) {
//                myPageViewModel.setResponseChangePassword(ChangePasswordRequest(
//                    password = passwordEditText.text.toString(),
//                    checkPassword = passwordCheckEditText.text.toString()
//                ))
//            }
//        }
    }
}