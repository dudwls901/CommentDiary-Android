package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageSignoutBinding
import com.movingmaker.commentdiary.common.CodaSnackBar
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignOutFragment : BaseFragment<FragmentMypageSignoutBinding>(R.layout.fragment_mypage_signout), CoroutineScope {
    override val TAG: String = SignOutFragment::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeDatas()
    }

    private fun observeDatas(){
        myPageViewModel.errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        myPageViewModel.snackMessage.observe(viewLifecycleOwner){
            CodaSnackBar.make(binding.root, it).show()
        }
    }

    private fun initViews() = with(binding) {
        signOutButton.setOnClickListener {
            myPageViewModel.signOut()
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}