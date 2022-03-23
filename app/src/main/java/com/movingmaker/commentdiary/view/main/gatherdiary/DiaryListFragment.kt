package com.movingmaker.commentdiary.view.main.gatherdiary

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentGatherdiaryDiarylistBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DiaryListFragment : BaseFragment(), CoroutineScope, OnDiarySelectListener {
    override val TAG: String = DiaryListFragment::class.java.simpleName

    private lateinit var binding: FragmentGatherdiaryDiarylistBinding
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by activityViewModels()
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private lateinit var diaryListAdapter: DiaryListAdapter

    private var searchPeriod="all"

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
        initViews()


        return binding.root
    }

    private fun observeDatas() {

        fragmentViewModel.fragmentState.observe(viewLifecycleOwner){ fragment->
            if(fragment=="gatherDiary"){
                setDiaries()
            }
        }
        
        gatherDiaryViewModel.responseGetAllDiary.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                Log.d(TAG, "observeDatas: ${it.body()!!.result}")
                it.body()?.result?.let { diaryList -> gatherDiaryViewModel.setDiaryList(diaryList) }
            } else {
                //todo 에러 처리
            }
        }
        gatherDiaryViewModel.responseGetMonthDiary.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                it.body()?.result?.let { diaryList -> gatherDiaryViewModel.setDiaryList(diaryList) }
            } else {
                //todo 에러 처리
            }
        }

        gatherDiaryViewModel.diaryList.observe(viewLifecycleOwner) { list ->
            binding.noDiaryTextView.isVisible = list.isEmpty()
            diaryListAdapter.submitList(list)
        }
    }

    private fun setDiaries() {
        Log.d(TAG, "setDiaries: ${searchPeriod}")
        launch(coroutineContext) {
            binding.loadingBar.isVisible = true
            launch(Dispatchers.IO) {
                when (searchPeriod) {
                    "all" -> {
                        gatherDiaryViewModel.setResponseGetAllDiary()
                    }
                    else -> {
                        gatherDiaryViewModel.setResponseGetMonthDiary(searchPeriod)
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        diaryListAdapter = DiaryListAdapter(this@DiaryListFragment)
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
        val (y, m) = gatherDiaryViewModel.selectedMonth.value!!.split('.')
        yearPicker.value = y.toInt()
        monthPicker.value = m.toInt()
//        val assetManager = resources.assets
//        val typeface = Typeface.createFromAsset(assetManager, "font/robotomedium.ttf")

        val typeface = resources.getFont(R.font.robotomedium)
        setNumberPickerStyle(yearPicker, getColor(requireContext(), R.color.text_black), typeface)
        setNumberPickerStyle(monthPicker, getColor(requireContext(), R.color.text_black), typeface)
        saveButton.setOnClickListener {
            // 날짜로 일기 불러오기 검색
            val date = "${yearPicker.value}.${String.format("%02d", monthPicker.value)}"
            searchPeriod = date
            setDiaries()
            gatherDiaryViewModel.setSelectedMonth(date)
            binding.selectDateTextView.text =
                "${yearPicker.value}년 ${String.format("%02d", monthPicker.value)}월"
            dialogView.dismiss()
        }

        allPeriodButton.setOnClickListener {
            // 전체 보기
            searchPeriod = "all"
            setDiaries()
            gatherDiaryViewModel.setSelectedMonth(DateConverter.ymFormat(DateConverter.getCodaToday()))
            binding.selectDateTextView.text = getString(R.string.show_all)
            dialogView.dismiss()
        }

    }

    override fun onDiarySelectListener(diary: Diary) {

        myDiaryViewModel.setSelectedDiary(diary)
        val nextDate = DateConverter.ymdToDate(diary.date)
        val nextDateToString = nextDate.plusDays(1).toString().replace('-','.')
        Log.d(TAG, "onDiarySelectListener: nextdate $nextDateToString")
        launch(coroutineContext) {
            myDiaryViewModel.setResponseGetDayComment(nextDateToString)
        }
//        myDiaryViewModel.setSelectedDate(diary.date)
        fragmentViewModel.setBeforeFragment("gatherDiary")
        Log.d(TAG, "initToolBar: ${fragmentViewModel.beforeFragment.value}")
        fragmentViewModel.setFragmentState("commentDiaryDetail")
    }

    // 넘버 픽커 텍스트 색깔 설정하는 함수
    @SuppressLint("DiscouragedPrivateApi")
    private fun setNumberPickerStyle(numberPicker: NumberPicker, color: Int, typeface: Typeface) {
        //글자 포커싱되어 수정하지 못하게
        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Log.d(TAG, "setNumberPickerStyle: downversion")
            val count = numberPicker.childCount
            for (i in 0..count) {
                val child = numberPicker.getChildAt(i)
                if (child is TextView) {
                    try {
                        child.setTextColor(color)
//                        child.typeface = typeface
                        numberPicker.invalidate()
//                        Log.d(TAG, "setNumberPickerText: downversion ${child::class.java.simpleName}")
                        var selectorWheelPaintField =
                            numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
                        var accessible = selectorWheelPaintField.isAccessible
                        selectorWheelPaintField.isAccessible = true
                        (selectorWheelPaintField.get(numberPicker) as Paint).color = color
                        selectorWheelPaintField.isAccessible = accessible
                        (selectorWheelPaintField.get(numberPicker) as Paint).typeface = typeface

                        numberPicker.invalidate()
                        var selectionDividerField =
                            numberPicker.javaClass.getDeclaredField("mSelectionDivider")
                        accessible = selectionDividerField.isAccessible
                        selectionDividerField.isAccessible = true
                        selectionDividerField.set(numberPicker, null)
                        selectionDividerField.isAccessible = accessible
                        (selectionDividerField.get(numberPicker) as Paint).typeface = typeface
                        numberPicker.invalidate()
                    } catch (exception: Exception) {
                        Log.d("test", "exception $exception")
                    }
                }
            }
        } else {
            Log.d(TAG, "setNumberPickerStyle: upversion")
            numberPicker.textColor = color
            val count = numberPicker.childCount
            for (i in 0..count) {
                val child = numberPicker.getChildAt(i)
                try {
                    if(child is TextView) {
//                        child.typeface = typeface
                        val paint = Paint()
                        paint.typeface = typeface
                        numberPicker.setLayerPaint(paint)
                        numberPicker.invalidate()
                    }
                } catch (exception: Exception) {
                    Log.d("test", "exception $exception")
                }

                numberPicker.invalidate()
            }
        }
    }

}