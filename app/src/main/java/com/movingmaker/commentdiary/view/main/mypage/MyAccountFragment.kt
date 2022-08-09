package com.movingmaker.commentdiary.view.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MyAccountFragment: BaseFragment<FragmentMypageMyaccountBinding>(R.layout.fragment_mypage_myaccount), CoroutineScope {
    override val TAG: String = MyAccountFragment::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.MY_ACCOUNT)
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

    private fun initViews() = with(binding){
        logoutLayout.setOnClickListener {
            myPageViewModel.logout()
        }
        signOutLayout.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToSignOutFragment()
            findNavController().navigate(action)
        }
        changePasswordLayout.setOnClickListener {
            val action = MyAccountFragmentDirections.actionMyAccountFragmentToChangePasswordFragment()
            findNavController().navigate(action)
        }
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}