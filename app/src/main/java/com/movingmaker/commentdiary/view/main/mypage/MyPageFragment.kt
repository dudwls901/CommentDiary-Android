package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage), CoroutineScope {
    override val TAG: String = MyPageFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    private var temperatureBarMaxWidthPx = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.MY_PAGE)
        myPageViewModel.setResponseGetMyPage()
        bindButtons()
        observeDatas()

    }

    private fun observeDatas() {

        myPageViewModel.errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        myPageViewModel.snackMessage.observe(viewLifecycleOwner){
            CodaSnackBar.make(binding.root, it ).show()
        }

        myPageViewModel.temperature.observe(viewLifecycleOwner){ temperature ->
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
        if(temperatureBarMaxWidthPx==0){
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