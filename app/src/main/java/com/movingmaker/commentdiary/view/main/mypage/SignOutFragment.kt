package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageSignoutBinding
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SignOutFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = SignOutFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object{
        fun newInstance() : SignOutFragment {
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

    private fun observeDatas(){
        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseSignOut.observe(lifecycleOwner) {
//                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    logOut()
                } else {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
//                    Toast.makeText(requireContext(), "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseLogOut.observe(lifecycleOwner) {
//                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    logOut()
                } else {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
//                    Toast.makeText(requireContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }

            }
        }

//        binding.lifecycleOwner?.let { lifecycleOwner ->
//            myPageViewModel.responseChangePassword.observe(lifecycleOwner) {
////                binding.loadingBar.isVisible = false
//                //todo 비밀번호 생성 규칙 처리
//                if (it.isSuccessful) {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
////                    Toast.makeText(requireContext(), "비밀번호 변경 성공!!!!!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Log.d(TAG, it.isSuccessful.toString())
//                    Log.d(TAG, it.body()?.code.toString())
//                    Log.d(TAG, it.body()?.message.toString())
////                    Toast.makeText(requireContext(), "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }


    }

    private fun initViews() = with(binding){
        signOutButton.setOnClickListener {
            launch(coroutineContext) {
                myPageViewModel.setResponseSignOut()
            }
        }
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myAccount")
        }
    }

    private fun logOut(){
        Log.d(TAG, "logOut: datastore 토큰 삭제 완료")
        CodaApplication.getInstance().logOut()
    }

}