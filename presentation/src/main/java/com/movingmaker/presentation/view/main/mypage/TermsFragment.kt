package com.movingmaker.presentation.view.main.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMypageTermsBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.POLICY_URL
import com.movingmaker.presentation.util.TERMS_URL
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentMypageTermsBinding>(R.layout.fragment_mypage_terms) {

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