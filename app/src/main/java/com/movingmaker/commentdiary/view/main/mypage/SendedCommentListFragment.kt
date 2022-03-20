package com.movingmaker.commentdiary.view.main.mypage

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMypageBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageMyaccountBinding
import com.movingmaker.commentdiary.databinding.FragmentMypageSendedCommentListBinding
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.view.main.gatherdiary.DiaryListFragment
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SendedCommentListFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = SendedCommentListFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980

        fun newInstance(): SendedCommentListFragment {
            return SendedCommentListFragment()
        }
    }

    private lateinit var binding: FragmentMypageSendedCommentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMypageSendedCommentListBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mypageviewmodel= myPageViewModel
        setDiaries("all")
        observeDatas()
        initViews()
        return binding.root
    }

    private fun observeDatas() {
        myPageViewModel.responseGetAllComment.observe(viewLifecycleOwner) {
            Log.d(TAG, "observeDatas: ????? ${it}")
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                Log.d(TAG, "observeDatas: ${it.body()!!.result}")
                it.body()?.result?.let { commentList -> myPageViewModel.setCommentList(commentList) }
            } else {
                //todo 에러 처리
            }
        }
        myPageViewModel.responseGetMonthComment.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                it.body()?.result?.let { commentList -> myPageViewModel.setCommentList(commentList) }
            } else {
                //todo 에러 처리
            }
        }
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            fragmentViewModel.setFragmentState("myPage")
        }
        selectDateLayout.setOnClickListener {
            showDialog()
        }
    }

    private fun setDiaries(date: String) {
        launch(coroutineContext) {
            binding.loadingBar.isVisible = true
            launch(Dispatchers.IO) {
                when (date) {
                    "all" -> {
                        myPageViewModel.setResponseGetAllComment()
                    }
                    else -> {
                        myPageViewModel.setResponseGetMonthComment(date)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {

        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_common_select_date)

        dialogView.show()

        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val allPeriodButton = dialogView.findViewById<Button>(R.id.allPeriodButton)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        val (y,m) = myPageViewModel.selectedMonth.value!!.split('.')
        yearPicker.value = y.toInt()
        monthPicker.value = m.toInt()

        saveButton.setOnClickListener {
            // 날짜로 일기 불러오기 검색
            val date = "${yearPicker.value}.${String.format("%02d",monthPicker.value)}"
            setDiaries(date)
            myPageViewModel.setSelectedMonth(date)
            binding.selectDateTextView.text = "${yearPicker.value}년 ${String.format("%02d",monthPicker.value)}월"
            dialogView.dismiss()
        }

        allPeriodButton.setOnClickListener {
            // 전체 보기
            setDiaries("all")
            myPageViewModel.setSelectedMonth(DateConverter.ymFormat(DateConverter.getCodaToday()))
            binding.selectDateTextView.text = getString(R.string.show_all)
            dialogView.dismiss()
        }

    }
}
