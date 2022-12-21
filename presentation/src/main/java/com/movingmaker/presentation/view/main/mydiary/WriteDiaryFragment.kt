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
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymdToDate
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

    /*
    * 혼자 쓰는 일기 수정
    * 코멘트일기 작성, 그냥 일기 작성
    * 코멘트 일기는 onStop에서 room에 임시저장시키기
    * 혼자 쓰는 일기 저장
    * 코멘트 일기 전송 임시 저장
    * 오늘 날짜인 경우만 topsheet 가능 지난 날짜는 혼자 쓴 일기 저장만 가능능    * */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineLifecycleScope = viewLifecycleOwner.lifecycleScope
        binding.vm = myDiaryViewModel
        binding.selectDiaryTypeSheet.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.WRITE_DIARY)

        //selectedDiary는 Null, selectedDate는 있음
        setDiaryType()
        initViews()
        initToolbar()
        observeDatas()
    }

    override fun onStop() {
        super.onStop()
        coroutineLifecycleScope.launch{
            myDiaryViewModel.handleAloneDiary()
        }
    }

    private fun setDiaryType() {
        //이전 날짜면은 AlONE_DIARY
        myDiaryViewModel.selectedDate.value?.let { selectedDate ->
            ymdToDate(selectedDate)?.let { selectedLocalDate ->
                //다이어리 타입 설정
                myDiaryViewModel.setSelectedDiaryType(
                    when {
                        selectedLocalDate < getCodaToday() -> {
                            DIARY_TYPE.ALONE_DIARY
                        }
                        else -> DIARY_TYPE.COMMENT_DIARY
                    }
                )
            }
        }
    }

    private fun observeDatas() = with(binding) {

        myDiaryViewModel.aloneDiary.observe(viewLifecycleOwner){
            Timber.d("aloneDiary ${it}")
        }
        //저장은 혼자 쓴 일기, 코멘트 일기 둘 다 가능
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner) { diary ->
            Timber.d(" selectedDiaryObserve $diary")
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

        backButton.setOnClickListener {
            when (myDiaryViewModel.selectedDiaryType.value) {
                DIARY_TYPE.ALONE_DIARY -> {
//                    Timber.d(
//                        "initViews: ${myDiaryViewModel.diaryContentText.value} ${myDiaryViewModel.diaryHeadText.value}"
//                    )
//                    if (myDiaryViewModel.diaryContent.value.isNullOrEmpty() && myDiaryViewModel.diaryHead.value.isNullOrEmpty()) {
                        findNavController().popBackStack()
//                    } else {
//                        showBackDialog()
//                    }
                }
                else -> findNavController().popBackStack()
            }
        }

//        sendButton.setOnClickListener {
//            if(myDiaryViewModel.diaryHead.value.isNullOrBlank()){
//                CodaSnackBar.make(binding.root, getString(R.string.write_diary_head_hint)).show()
//            }else if(myDiaryViewModel.diaryContent.value!!.length  < DIARY_CONTENT_MINIMUM_LENGTH ){
//                CodaSnackBar.make(binding.root, getString(R.string.write_comment_diary_notice)).show()
//            }else{
//                coroutineLifecycleScope.launch {
//                    when (myDiaryViewModel.saveDiary('Y')) {
//                        true -> showCircleDialog()
//                        else -> {}
//                    }
//                }
//            }
//        }
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
        myDiaryViewModel.diaryHead.value = ""
        myDiaryViewModel.diaryContent.value = ""
        myDiaryViewModel.closeSelectDiaryTypeToolbarIsExpanded()
        super.onDestroyView()
    }
}