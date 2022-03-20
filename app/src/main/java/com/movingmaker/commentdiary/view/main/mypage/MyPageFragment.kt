package com.movingmaker.commentdiary.view.main.mypage

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyPageFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = MyPageFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private lateinit var display: Display
    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
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
        binding.mypageviewmodel = myPageViewModel
        initViews()
        bindButtons()
        observeDatas()

        return binding.root
    }

    private fun observeDatas() {
        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseSignOut.observe(lifecycleOwner) {
//                binding.loadingBar.isVisible = false
                if (it.isSuccessful) {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
                } else {
                    Log.d(TAG, it.isSuccessful.toString())
                    Log.d(TAG, it.body()?.code.toString())
                    Log.d(TAG, it.body()?.message.toString())
//                    Toast.makeText(requireContext(), "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

            }
        }
        binding.lifecycleOwner?.let { lifecycleOwner ->
            myPageViewModel.responseGetMyPage.observe(lifecycleOwner) { response ->
                //마이 페이지 불러오기 성공시
                if (response.isSuccessful) {
                    myPageViewModel.setMyAccount(response.body()!!.result.email)
                    myPageViewModel.setTemperature(response.body()!!.result.temperature)
                    setTemperatureBar()
                }
                //마이 페이지 불러오기 실패
                else {
                    Toast.makeText(requireContext(), "내 정보를 불러오는 데 실패하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        launch(coroutineContext) {
            myPageViewModel.setResponseGetMyPage()
        }
    }

    private fun bindButtons() = with(binding){
        myAccountLayout.setOnClickListener {
            fragmentViewModel.setFragmentState("myAccount")
        }
        termsAndPolicyLayout.setOnClickListener {
            fragmentViewModel.setFragmentState("terms")
        }
        myCommentLayout.setOnClickListener {
            fragmentViewModel.setFragmentState("sendedCommentList")
        }
    }

    private fun setTemperatureBar() = with(binding){
        //dp값 구하기
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
//            display = requireActivity().display!!
//            display.getRealMetrics()
//        }
//        else{
//            display = requireActivity().windowManager.defaultDisplay
//        }
//        val displayMetrics = DisplayMetrics()
//        val dpi = displayMetrics.densityDpi
//        val density = displayMetrics.density
        //값이 없거나 0.0인 경우 0dp로 들어가서 css,cee parent에 의해 꽉차게 되는 것을 1.0으로 방지
        var temperature = myPageViewModel.temperature.value?: 1.0
        if(temperature==0.0){
            temperature=1.0
        }
        //뷰의 maxWidthPx
        val maxWidthPx = temperatureBar.width
        val temperatureRateForPx = (maxWidthPx * (temperature/100.0))
        //temperatureBar의 상위 viewgroup에서 마진 설정 가능
        val layoutParams : ConstraintLayout.LayoutParams = temperatureBar.layoutParams as ConstraintLayout.LayoutParams
        //6dp = 18px, 해상도별로 다름
        layoutParams.setMargins(17,10,17,10)
        layoutParams.width = temperatureRateForPx.toInt()
        layoutParams.height = 17
        temperatureBar.layoutParams = layoutParams

        //invisible로 가려놨다가 유저 temperature 불러오고 계산, 셋팅 끝난 후 보여주기
        temperatureBar.isVisible = true

//        Log.d(TAG, "setTemperatureBar: ${displayMetrics.densityDpi} ${displayMetrics.density}")
        Log.d(TAG, "setTemperatureBar: ${temperatureBar.width} ${temperatureBar.layoutParams.width}")
        Log.d(TAG, "setTemperatureBar: ${temperatureBar.height} ${temperatureBar.layoutParams.height}")
    }
}