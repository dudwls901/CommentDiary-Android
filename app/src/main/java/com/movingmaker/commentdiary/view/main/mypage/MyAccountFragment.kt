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
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyAccountFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = MyAccountFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object{
        fun newInstance() : MyAccountFragment {
            return MyAccountFragment()
        }
    }

    private lateinit var binding: FragmentMypageMyaccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageMyaccountBinding.inflate(layoutInflater)

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

    }

    private fun initViews() = with(binding){
        logoutLayout.setOnClickListener {
            launch(coroutineContext) {
                myPageViewModel.setResponseLogOut()
            }
        }
        signOutLayout.setOnClickListener {
            fragmentViewModel.setBeforeFragment("myAccount")
            fragmentViewModel.setFragmentState("signOut")
        }
        changePasswordLayout.setOnClickListener {
            fragmentViewModel.setBeforeFragment("myAccount")
            fragmentViewModel.setFragmentState("changePassword")
        }
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myPage")
        }
    }

    private fun logOut(){
        Log.d(TAG, "logOut: datastore 토큰 삭제 완료")
        CodaApplication.getInstance().logOut()
    }

}