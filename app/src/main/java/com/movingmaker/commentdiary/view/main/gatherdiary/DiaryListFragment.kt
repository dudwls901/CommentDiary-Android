package com.movingmaker.commentdiary.view.main.gatherdiary

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
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentGatherdiaryDiarylistBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import okhttp3.internal.notifyAll
import java.util.*
import kotlin.coroutines.CoroutineContext

class DiaryListFragment : BaseFragment(), CoroutineScope, OnDiarySelectListener {
    override val TAG: String = DiaryListFragment::class.java.simpleName

    private lateinit var binding: FragmentGatherdiaryDiarylistBinding
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by activityViewModels()
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private lateinit var diaryListAdapter: DiaryListAdapter


    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980

        fun newInstance(): DiaryListFragment {
            return DiaryListFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGatherdiaryDiarylistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gatherDiaryviewModel = gatherDiaryViewModel
        observeDatas()
        setDiaries("all")
        initViews()


        return binding.root
    }

    private fun observeDatas() {
        gatherDiaryViewModel.responseGetAllDiary.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                Log.d(TAG, "observeDatas: ${it.body()!!.result}")
                it.body()?.result?.let { diaryList -> gatherDiaryViewModel.setDiaryList(diaryList) }
            }
            else{
                //todo 에러 처리
            }
        }
        gatherDiaryViewModel.responseGetMonthDiary.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                it.body()?.result?.let { diaryList -> gatherDiaryViewModel.setDiaryList(diaryList) }
            }
            else{
                //todo 에러 처리
            }
        }

        gatherDiaryViewModel.diaryList.observe(viewLifecycleOwner){ list->
            binding.noDiaryTextView.isVisible = list.isEmpty()
            diaryListAdapter.submitList(list)
        }
    }

    private fun setDiaries(date: String) {

        launch(coroutineContext) {
            binding.loadingBar.isVisible = true
            launch(Dispatchers.IO){
                when(date){
                    "all"->{
                        gatherDiaryViewModel.setResponseGetAllDiary()
                    }
                    else->{
                        gatherDiaryViewModel.setResponseGetMonthDiary(date)
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        diaryListAdapter = DiaryListAdapter(emptyList(),this@DiaryListFragment)
        diaryListAdapter.setHasStableIds(true)
        binding.diaryListRecyclerView.adapter = diaryListAdapter
        selectDateLayout.setOnClickListener {
            showDialog()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showDialog() {

        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_gatherdiary_select_date)

        dialogView.show()

        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val allPeriodButton = dialogView.findViewById<Button>(R.id.allPeriodButton)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        val (y,m) = gatherDiaryViewModel.selectedMonth.value!!.split('.')
        yearPicker.value = y.toInt()
        monthPicker.value = m.toInt()

        saveButton.setOnClickListener {
            // 날짜로 일기 불러오기 검색
            val date = "${yearPicker.value}.${String.format("%02d",monthPicker.value)}"
            setDiaries(date)
            gatherDiaryViewModel.setSelectedMonth(date)
            binding.selectDateTextView.text = "${yearPicker.value}년 ${String.format("%02d",monthPicker.value)}월"
            dialogView.dismiss()
        }

        allPeriodButton.setOnClickListener {
            // 전체 보기
            setDiaries("all")
            gatherDiaryViewModel.setSelectedMonth(DateConverter.ymFormat(DateConverter.getCodaToday()))
            binding.selectDateTextView.text = getString(R.string.show_all)
            dialogView.dismiss()
        }

    }

    override fun onDiarySelectListener(diary: Diary) {
        myDiaryViewModel.setSelectedDiary(diary)
        fragmentViewModel.setBeforeFragment("gatherDiary")
        fragmentViewModel.setFragmentState("commentDiaryDetail")
    }

}