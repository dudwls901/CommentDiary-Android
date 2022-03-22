package com.movingmaker.commentdiary.view.main.mypage

import android.content.Intent
import android.net.Uri
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

        return binding.root
    }

    private fun initViews() = with(binding){

        policyLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CodaApplication.policyUrl))
            startActivity(intent)
        }
        termsLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CodaApplication.termsUrl))
            startActivity(intent)
        }

        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myPage")
        }

    }
}