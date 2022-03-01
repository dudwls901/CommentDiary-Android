package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.view.onboarding.OnboardingLoginActivity
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TempMyPageFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = TempMyPageFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    companion object{
        fun newInstance() : TempMyPageFragment {
            return TempMyPageFragment()
        }
    }

    private lateinit var binding: FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageBinding.inflate(layoutInflater)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //붙여야 observer가
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
                    Toast.makeText(requireContext(), "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseChangePassword.observe(lifecycleOwner) {
//                binding.loadingBar.isVisible = false
                //todo 비밀번호 생성 규칙 처리
                if (it.isSuccessful) {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    Toast.makeText(requireContext(), "비밀번호 변경 성공!!!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    Toast.makeText(requireContext(), "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun initViews() = with(binding){
        signOutButton.setOnClickListener {
            launch(coroutineContext) {
                myPageViewModel.setResponseSignOut()
            }
        }
        changePasswordButton.setOnClickListener {
            launch(coroutineContext) {
                myPageViewModel.setResponseChangePassword(ChangePasswordRequest(
                    password = passwordEditText.text.toString(),
                    checkPassword = passwordCheckEditText.text.toString()
                ))
            }
        }
    }

    private fun logOut(){


        //Todo 값 삭제
        CodaApplication.getInstance().getDataStore().insertAuth("", "", 0L)

        startActivity(Intent(requireContext(), OnboardingLoginActivity::class.java).apply {
            //메인 액티비티 실행하면 현재 화면 필요 없으니 cleartask
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

}