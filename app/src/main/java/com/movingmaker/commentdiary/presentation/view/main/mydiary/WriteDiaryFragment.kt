package com.movingmaker.commentdiary.presentation.view.main.mydiary

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.CodaSnackBar
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.DIARY_TYPE
import com.movingmaker.commentdiary.common.util.DateConverter
import com.movingmaker.commentdiary.common.util.EventObserver
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWritediaryBinding
import com.movingmaker.commentdiary.presentation.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.presentation.viewmodel.mydiary.MyDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WriteDiaryFragment :
    BaseFragment<FragmentMydiaryWritediaryBinding>(R.layout.fragment_mydiary_writediary) {

    override val TAG: String = WriteDiaryFragment::class.java.simpleName
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()


    /*
    * 혼자 쓰는 일기 수정
    * 코멘트일기 작성, 그냥 일기 작성
    * 코멘트 일기는 onStop에서 room에 임시저장시키기
    * 혼자 쓰는 일기 저장
    * 코멘트 일기 전송 임시 저장
    * 오늘 날짜인 경우만 topsheet 가능 지난 날짜는 혼자 쓴 일기 저장만 가능능    * */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        binding.selectDiaryTypeSheet.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.WRITE_DIARY)

        //selectedDiary는 Null, selectedDate는 있음
        Timber.d("onViewCreated: writeDiary " + myDiaryViewModel.selectedDiary.value + " " + myDiaryViewModel.selectedDate.value + " " + myDiaryViewModel.selectedYearMonth.value)
        initViews()
        initToolbar()
        observeDatas()
    }

    private fun observeDatas() = with(binding) {

        myDiaryViewModel.snackMessage.observe(viewLifecycleOwner, EventObserver {
            CodaSnackBar.make(binding.root, it).show()
        })

        //저장은 혼자 쓴 일기, 코멘트 일기 둘 다 가능
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner) { diary ->
            //이전 날짜면은 AlONE_DIARY
            val selectedDate = myDiaryViewModel.selectedDate.value
            selectedDate
            //다이어리 타입 설정
            myDiaryViewModel.setSelectedDiaryType(
                when {
                    myDiaryViewModel.selectedDate.value != null && DateConverter.ymdToDate(
                        myDiaryViewModel.selectedDate.value!!
                    ) < DateConverter.getCodaToday() -> {
                        DIARY_TYPE.ALONE_DIARY
                    }
                    diary == null -> DIARY_TYPE.COMMENT_DIARY
                    else -> DIARY_TYPE.ALONE_DIARY
                }
            )
        }

        myDiaryViewModel.selectDiaryTypeToolbarIsExpanded.observe(viewLifecycleOwner) { isExpand ->
            handleDiarySheet(isExpand)
        }

    }

    private fun initToolbar() = with(binding) {
        diaryTypeTextView.setOnClickListener {
            myDiaryViewModel.changeSelectDiaryTypeToolbarIsExpanded()
        }
    }

    private fun initViews() = with(binding) {

        diaryHeadEditText.addTextChangedListener {
            myDiaryViewModel.setDiaryHeadText(it.toString())
        }

        diaryContentEditText.addTextChangedListener {
            myDiaryViewModel.setDiaryContentText(it.toString())
        }

        backButton.setOnClickListener {
            when (myDiaryViewModel.selectedDiaryType.value) {
                DIARY_TYPE.ALONE_DIARY -> {
                    Timber.d(
                        "initViews: ${myDiaryViewModel.diaryContentText.value} ${myDiaryViewModel.diaryHeadText.value}"
                    )
                    if (myDiaryViewModel.diaryContentText.value.isNullOrEmpty() && myDiaryViewModel.diaryHeadText.value.isNullOrEmpty()) {
                        findNavController().popBackStack()
                    } else {
                        showBackDialog()
                    }
                }
                else -> findNavController().popBackStack()
            }
        }

        saveButton.setOnClickListener {
            if (diaryHeadEditText.text.isNullOrBlank() || diaryContentEditText.text.isNullOrBlank()) {
                CodaSnackBar.make(binding.root, "내용을 입력해 주세요").show()
            } else {
                lifecycleScope.launch {
                    when (myDiaryViewModel.saveDiary('N')) {
                        true -> {
                            val action =
                                WriteDiaryFragmentDirections.actionWriteDiaryFragmentToAloneDiaryDetailFragment()
                            findNavController().navigate(action)
                        }
                        else -> {}
                    }
                }
            }
        }

        sendButton.setOnClickListener {
            lifecycleScope.launch {
                when (myDiaryViewModel.saveDiary('Y')) {
                    true -> showCircleDialog()
                    else -> {}
                }
            }
        }
    }


    private fun showBackDialog() {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_mydiary_writediary)
        dialogView.setCancelable(false)

        dialogView.show()


        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val noticeTextView = dialogView.findViewById<TextView>(R.id.noticeTextView)
        noticeTextView.text = getString(R.string.write_diary_back)

        submitButton.setOnClickListener {
            findNavController().popBackStack()
            dialogView.dismiss()
        }

        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }


    private fun handleDiarySheet(isExpand: Boolean) = with(binding) {
        if (isExpand) {
            wrapperLayout.isVisible = true
            selectDiaryTypeSheet.root.visibility = View.VISIBLE
            val alphaIn: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_in)
            wrapperLayout.startAnimation(alphaIn)
            val scaleIn: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in)
            selectDiaryTypeSheet.root.startAnimation(scaleIn)
        } else {
            wrapperLayout.isVisible = false
            val alphaOut: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_out)
            wrapperLayout.startAnimation(alphaOut)
            val scaleOut: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.scale_out)
            selectDiaryTypeSheet.root.startAnimation(scaleOut)
            selectDiaryTypeSheet.root.visibility = View.GONE
        }
    }

    private suspend fun showCircleDialog() {

        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_mydiary_success_notice)
        dialogView.setCancelable(false)
        dialogView.show()
        delay(2000L)
        dialogView.dismiss()
        val action =
            WriteDiaryFragmentDirections.actionWriteDiaryFragmentToCommentDiaryDetailFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        myDiaryViewModel.setDiaryHeadText("")
        myDiaryViewModel.setDiaryContentText("")
        myDiaryViewModel.closeSelectDiaryTypeToolbarIsExpanded()
        super.onDestroyView()
    }
}