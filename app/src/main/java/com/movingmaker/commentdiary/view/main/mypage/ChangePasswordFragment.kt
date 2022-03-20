package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageChangePasswordBinding
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

class ChangePasswordFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = ChangePasswordFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object {
        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }

    private lateinit var binding: FragmentMypageChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageChangePasswordBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mypageviewmodel = myPageViewModel
        initViews()
        observeDatas()

        return binding.root
    }

    private fun observeDatas() {

        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseChangePassword.observe(lifecycleOwner) {
                //todo 비밀번호 생성 규칙 처리
                if (it.isSuccessful) {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                    Toast.makeText(requireContext(), "비밀번호를 변경하였습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
                fragmentViewModel.setFragmentState("myPage")
            }
        }


    }

    private fun initViews() = with(binding) {
        changePasswordButton.setOnClickListener {
            launch(coroutineContext) {
                myPageViewModel.setResponseChangePassword(
                    ChangePasswordRequest(
                        password = passwordEditText.text.toString(),
                        checkPassword = passwordCheckEditText.text.toString()
                    )
                )
            }
        }
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myAccount")
        }

        passwordEditText.addTextChangedListener {
            var hasLetter = false
            var hasNum = false
            var hasSign = false
            for (ch in passwordEditText.text.toString()) {
                when {
                    ch.isLetter() -> hasLetter = true
                    ch.isDigit() -> hasNum = true
                    else -> hasSign = true
                }
            }
            val isPasswordCorrect =
                !(passwordEditText.text.isNotEmpty() && (passwordEditText.text.length < 8 || !hasLetter || !hasNum || !hasSign || passwordEditText.text.length>16))
            val isPasswordCheckCorrect =
                !(passwordCheckEditText.text.isNotEmpty() && (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            myPageViewModel.setPasswordCorrect(isPasswordCorrect)
            myPageViewModel.setPasswordCheckCorrect(isPasswordCheckCorrect)
        }
        passwordCheckEditText.addTextChangedListener {
            val isPasswordCheckCorrect = !(passwordCheckEditText.text.isNotEmpty() &&
                    (passwordEditText.text.toString() != passwordCheckEditText.text.toString()))

            myPageViewModel.setPasswordCheckCorrect(isPasswordCheckCorrect)
        }
    }


}