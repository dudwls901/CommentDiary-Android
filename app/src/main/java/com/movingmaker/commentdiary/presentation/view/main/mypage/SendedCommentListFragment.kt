package com.movingmaker.commentdiary.presentation.view.main.mypage

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentMypageSendedCommentListBinding
import com.movingmaker.commentdiary.presentation.base.BaseFragment
import com.movingmaker.commentdiary.presentation.util.DateConverter
import com.movingmaker.commentdiary.presentation.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendedCommentListFragment :
    BaseFragment<FragmentMypageSendedCommentListBinding>(R.layout.fragment_mypage_sended_comment_list) {

    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    private var searchPeriod = "all"

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 1980
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myPageViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.SENDED_COMMENT_LIST)
        setComments()
        initViews()
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        selectDateLayout.setOnClickListener {
            showDialog()
        }
    }

    private fun setComments() {
        myPageViewModel.getResponseCommentList(searchPeriod)
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
        val (y, m) = myPageViewModel.selectedMonth.value!!.split('.')
        yearPicker.value = y.toInt()
        monthPicker.value = m.toInt()

        val typeface = resources.getFont(R.font.robotomedium)
        setNumberPickerStyle(
            yearPicker,
            ContextCompat.getColor(requireContext(), R.color.text_black), typeface
        )
        setNumberPickerStyle(
            monthPicker,
            ContextCompat.getColor(requireContext(), R.color.text_black), typeface
        )

        saveButton.setOnClickListener {
            // 날짜로 일기 불러오기 검색
            val date = "${yearPicker.value}.${String.format("%02d", monthPicker.value)}"
            searchPeriod = date
            setComments()
            myPageViewModel.setSelectedMonth(date)
            binding.selectDateTextView.text =
                "${yearPicker.value}년 ${String.format("%02d", monthPicker.value)}월"
            dialogView.dismiss()
        }

        allPeriodButton.setOnClickListener {
            // 전체 보기
            searchPeriod = "all"
            setComments()
            myPageViewModel.setSelectedMonth(DateConverter.ymFormat(DateConverter.getCodaToday())!!)
            binding.selectDateTextView.text = getString(R.string.show_all)
            dialogView.dismiss()
        }

    }

    // 넘버 픽커 텍스트 색깔 설정하는 함수
    @SuppressLint("DiscouragedPrivateApi")
    private fun setNumberPickerStyle(numberPicker: NumberPicker, color: Int, typeface: Typeface) {
        //글자 포커싱되어 수정하지 못하게
        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val count = numberPicker.childCount
            for (i in 0..count) {
                val child = numberPicker.getChildAt(i)
                if (child is TextView) {
                    try {
                        child.setTextColor(color)
                        numberPicker.invalidate()
                        val selectorWheelPaintField =
                            numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
                        var accessible = selectorWheelPaintField.isAccessible
                        selectorWheelPaintField.isAccessible = true
                        (selectorWheelPaintField.get(numberPicker) as Paint).color = color
                        selectorWheelPaintField.isAccessible = accessible
                        (selectorWheelPaintField.get(numberPicker) as Paint).typeface = typeface

                        numberPicker.invalidate()
                        val selectionDividerField =
                            numberPicker.javaClass.getDeclaredField("mSelectionDivider")
                        accessible = selectionDividerField.isAccessible
                        selectionDividerField.isAccessible = true
                        selectionDividerField.set(numberPicker, null)
                        selectionDividerField.isAccessible = accessible
                        (selectionDividerField.get(numberPicker) as Paint).typeface = typeface
                        numberPicker.invalidate()
                    } catch (exception: Exception) {
                    }
                }
            }
        } else {
//            Timber.d( "setNumberPickerStyle: upversion")
            numberPicker.textColor = color
            val count = numberPicker.childCount
            for (i in 0..count) {
                val child = numberPicker.getChildAt(i)
                try {
                    if (child is TextView) {
//                        child.typeface = typeface
                        val paint = Paint()
                        paint.typeface = typeface
                        numberPicker.setLayerPaint(paint)
                        numberPicker.invalidate()
                    }
                } catch (exception: Exception) {
                }

                numberPicker.invalidate()
            }
        }
    }
}

