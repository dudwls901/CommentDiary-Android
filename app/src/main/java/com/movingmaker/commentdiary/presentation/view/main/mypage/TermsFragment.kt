package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.common.util.Url.POLICY_URL
import com.movingmaker.commentdiary.common.util.Url.TERMS_URL
import com.movingmaker.commentdiary.databinding.FragmentMypageTermsBinding
import com.movingmaker.commentdiary.presentation.viewmodel.FragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentMypageTermsBinding>(R.layout.fragment_mypage_terms) {
    override val TAG: String = TermsFragment::class.java.simpleName

    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.TERMS)
    }

    private fun initViews() = with(binding) {

        policyLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(POLICY_URL))
            startActivity(intent)
        }
        termsLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_URL))
            startActivity(intent)
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}