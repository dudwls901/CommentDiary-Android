package com.movingmaker.presentation.view.main.mydiary

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMydiaryWritediaryBinding
import com.movingmaker.presentation.util.DIARY_CONTENT_MINIMUM_LENGTH
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.getDiaryType
import com.movingmaker.presentation.util.ymdToDate
import com.movingmaker.presentation.view.snackbar.CodaSnackBar
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WriteDiaryFragment :
    BaseFragment<FragmentMydiaryWritediaryBinding>(R.layout.fragment_mydiary_writediary) {

    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private lateinit var coroutineLifecycleScope: LifecycleCoroutineScope
    private var isSendedDiary = false

    /*
    * 혼자 쓰는 일기 수정
    * 코멘트일기 작성, 그냥 일기 작성
    * 코멘트 일기는 onStop에서 room에 임시저장시키기
    * 혼자 쓰는 일기 저장
    * 코멘트 일기 전송 임시 저장
    * 오늘 날짜인 경우만 topsheet 가능 지난 날짜는 혼자 쓴 일기 저장만 가능능    */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineLifecycleScope = viewLifecycleOwner.lifecycleScope
        binding.vm = myDiaryViewModel
        binding.selectDiaryTypeSheet.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.WRITE_DIARY)
        initDatas()
        setDiaryType()
        initViews()
        initToolbar()
        observeDatas()
    }

    private fun initDatas() = with(myDiaryViewModel) {
        diaryHead.value = if (selectedDiary.value == null) "" else selectedDiary.value!!.title
        diaryContent.value = if (selectedDiary.value == null) "" else selectedDiary.value!!.content
    }

    override fun onStop() {
        super.onStop()
        with(myDiaryViewModel) {
            //todo, temp -> alone 글자 안 바꾸는 경우 false, temp -> alone 글자 바꿔서 저장시켜도 오류
//            Timber.e("임시저장 여부 before selected: ${ }")
            Timber.e(
                "임시저장 여부 ${
                    isSendedDiary.not() && (
                            (selectedDiary.value?.title == diaryHead.value && selectedDiary.value?.content == diaryContent.value).not()
                                    || selectedDiaryType.value != selectedDiary.value?.deliveryYN?.getDiaryType()
                            )
                }"
            )
            //서버에 저장시켜서 화면 나가는 경우 임시저장 방지
            //글자 안 바뀐 경우 임시저장 방지
            if (
                isSendedDiary.not() && (
                        (selectedDiary.value?.title == diaryHead.value && selectedDiary.value?.content == diaryContent.value).not()
                                || selectedDiaryType.value != selectedDiary.value?.deliveryYN?.getDiaryType()
                        )
            ) {
                myDiaryViewModel.handleDiary(myDiaryViewModel.selectedDiaryType.value)
            }
        }
    }

    private fun setDiaryType() = with(myDiaryViewModel) {
        selectedDate.value?.let { selectedDate ->
            ymdToDate(selectedDate)?.let { selectedLocalDate ->
                //다이어리 타입 설정
                setSelectedDiaryType(
                    when {
                        //이전 날짜면 AlONE_DIARY
                        selectedLocalDate < getCodaToday() -> {
                            DIARY_TYPE.ALONE_DIARY
                        }
                        else -> {
                            val diary = selectedDiary.value
                            //오늘 날짜인 경우
                            if (diary != null) {
                                //작성한 경우 deliveryYN에 따라 다름
                                if (diary.deliveryYN == 'N') {
                                    DIARY_TYPE.ALONE_DIARY
                                } else {
                                    DIARY_TYPE.COMMENT_DIARY
                                }
                            } else {
                                //작성 안 한 경우
                                DIARY_TYPE.COMMENT_DIARY
                            }
                        }
                    }
                )
            }
        }
    }

    private fun observeDatas() {
        myDiaryViewModel.selectDiaryTypeToolbarIsExpanded.observe(viewLifecycleOwner) { isExpand ->
            handleDiarySheet(isExpand)
        }
        //observe하지 않으면 항상 null
        myDiaryViewModel.aloneDiary.observe(viewLifecycleOwner) {
            /*no-op*/
        }
    }

    private fun initToolbar() = with(binding) {
        diaryTypeTextView.setOnClickListener {
            myDiaryViewModel.changeSelectDiaryTypeToolbarIsExpanded()
        }
    }

    private fun initViews() = with(binding) {

        backButton.setOnClickListener {
            when (myDiaryViewModel.selectedDiaryType.value) {
                DIARY_TYPE.ALONE_DIARY -> {
                    findNavController().popBackStack()
                }
                else -> findNavController().popBackStack()
            }
        }

        sendButton.setOnClickListener {
            if (myDiaryViewModel.diaryHead.value.isNullOrBlank()) {
                CodaSnackBar.make(binding.root, getString(R.string.write_diary_head_hint)).show()
            } else if (myDiaryViewModel.diaryContent.value!!.length < DIARY_CONTENT_MINIMUM_LENGTH) {
                CodaSnackBar.make(binding.root, getString(R.string.write_comment_diary_notice))
                    .show()
            } else {
                showSendCommentDiaryDialog()
            }
        }
    }

    private fun showSendCommentDiaryDialog() {
        val dialogView = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_mydiary_send_diary)
            setCancelable(false)
            show()
        }
        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        submitButton.setOnClickListener {
            coroutineLifecycleScope.launch {
                isSendedDiary = true
                dialogView.dismiss()
                myDiaryViewModel.sendCommentDiary()
                showCircleDialog()
            }
        }

        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

    private fun showBackDialog() {
        val dialogView = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_mydiary_send_diary)
            setCancelable(false)
            show()
        }


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
        myDiaryViewModel.diaryHead.value = ""
        myDiaryViewModel.diaryContent.value = ""
        myDiaryViewModel.closeSelectDiaryTypeToolbarIsExpanded()
        super.onDestroyView()
    }
}