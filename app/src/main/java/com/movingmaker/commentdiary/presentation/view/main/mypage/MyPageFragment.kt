package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.presentation.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    override val TAG: String = MyPageFragment::class.java.simpleName

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    private var temperatureBarMaxWidthPx = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.MY_PAGE)
        myPageViewModel.getMyPage()
        bindButtons()
        observeDatas()

    }

    private fun observeDatas() {

        myPageViewModel.temperature.observe(viewLifecycleOwner) { temperature ->
            setTemperatureBar(temperature)
        }
    }

    private fun bindButtons() = with(binding) {
        myAccountLayout.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToMyAccountFragment()
            findNavController().navigate(action)
        }
        termsAndPolicyLayout.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToTermsFragment()
            findNavController().navigate(action)
        }
        pushAlarmLayout.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToPushAlarmOnOffFragment()
            findNavController().navigate(action)
        }
        myCommentLayout.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToSendedCommentListFragment()
            findNavController().navigate(action)
        }
    }

    private fun setTemperatureBar(temp: Double) = with(binding) {

        //값이 없거나 0.0인 경우 0dp로 들어가서 css,cee parent에 의해 꽉차게 되는 것을 1.0으로 방지
        var temperature = temp
        if (temperature == 0.0) {
            temperature = 1.0
        }

        //뷰의 maxWidthPx
        if (temperatureBarMaxWidthPx == 0) {
            temperatureBarMaxWidthPx = temperatureBar.width
        }
        val temperatureRateForPx = (temperatureBarMaxWidthPx * (temperature / 100.0))
        //temperatureBar의 상위 viewgroup에서 마진 설정 가능
        val layoutParams: ConstraintLayout.LayoutParams =
            temperatureBar.layoutParams as ConstraintLayout.LayoutParams
        //6dp = 18px, 해상도별로 다름
        layoutParams.setMargins(17, 10, 17, 10)
        layoutParams.width = temperatureRateForPx.toInt()
        layoutParams.height = 17
        temperatureBar.layoutParams = layoutParams

        //invisible로 가려놨다가 유저 temperature 불러오고 계산, 셋팅 끝난 후 보여주기
        temperatureBar.isVisible = true

    }
}