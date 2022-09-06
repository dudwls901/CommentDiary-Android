package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWritediaryBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.util.DIARY_TYPE
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WriteDiaryFragment : BaseFragment<FragmentMydiaryWritediaryBinding>(R.layout.fragment_mydiary_writediary), CoroutineScope {

    override val TAG: String = WriteDiaryFragment::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        binding.selectDiaryTypeSheet.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.WRITE_DIARY)

        //selectedDiary는 Null, selectedDate는 있음
        Log.d(TAG, "onViewCreated: writeDiary ${myDiaryViewModel.selectedDiary.value} ${myDiaryViewModel.selectedDate.value} ${myDiaryViewModel.selectedYearMonth.value}")
        initViews()
        initToolbar()
        observeDatas()
    }

    private fun observeDatas() = with(binding) {

        //삭제한 경우 뒤로가기
        myDiaryViewModel.isDeletedDiary.observe(viewLifecycleOwner){
            if(it){
                findNavController().popBackStack()
            }
        }
        myDiaryViewModel.snackMessage.observe(viewLifecycleOwner){
            CodaSnackBar.make(binding.root, it).show()
        }

//        myDiaryViewModel.isSavedDiary.observe(viewLifecycleOwner){
//            //코멘트 일기 저장한 경우에 디테일 화면으로 이동
//            if(it){
//                if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='Y' &&
//                    myDiaryViewModel.selectedDiary.value!!.tempYN=='N') {
//                    val action =
//                        WriteDiaryFragmentDirections.actionWriteDiaryFragmentToCommentDiaryDetailFragment()
//                    findNavController().navigate(action)
//                }
//            }
//        }

        //저장은 혼자 쓴 일기, 코멘트 일기 둘 다 가능
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner){ diary->
            //다이어리 타입 설정
            myDiaryViewModel.setSelectedDiaryType(
                when{
                    diary == null || diary.deliveryYN == 'Y'  ->  DIARY_TYPE.COMMENT_DIARY
                    else -> DIARY_TYPE.ALONE_DIARY
                }
            )
        }

        myDiaryViewModel.selectedDiaryType.observe(viewLifecycleOwner){ diaryType ->
            Log.d(TAG, "observeDatas: ${diaryType}")
            changeViews(diaryType)
        }

        myDiaryViewModel.selectDiaryTypeToolbarIsExpanded.observe(viewLifecycleOwner){ isExpand ->
            if(isExpand){
                wrapperLayout.isVisible = true
                selectDiaryTypeSheet.root.visibility = View.VISIBLE
                val alphaIn: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_in)
                wrapperLayout.startAnimation(alphaIn)
                val scaleIn: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in)
                selectDiaryTypeSheet.root.startAnimation(scaleIn)
            } else{
                wrapperLayout.isVisible = false
                val alphaOut: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_out)
                wrapperLayout.startAnimation(alphaOut)
                val scaleOut: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_out)
                selectDiaryTypeSheet.root.startAnimation(scaleOut)
                selectDiaryTypeSheet.root.visibility = View.GONE
            }
        }

        //혼자쓴 일기, 임시 저장 일기 수정
        myDiaryViewModel.responseEditDiary.observe(viewLifecycleOwner){
//            binding.loadingBar.isVisible = false
//            if(it.isSuccessful){
//                CodaSnackBar.make(binding.root, "일기가 수정되었습니다.").show()
//                //다이어리 셋팅
//                myDiaryViewModel.setSelectedDiary(
//                    Diary(
//                        id = myDiaryViewModel.selectedDiary.value!!.id,
//                        title = diaryHeadEditText.text.toString(),
//                        content = diaryContentEditText.text.toString(),
//                        date = myDiaryViewModel.selectedDiary.value!!.date,
//                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
//                        tempYN = myDiaryViewModel.selectedDiary.value!!.tempYN,
//                        commentList = myDiaryViewModel.selectedDiary.value!!.commentList
//                    )
//                )
//
//                //임시 저장 수정이면
//                if(myDiaryViewModel.selectedDiary.value!!.tempYN=='Y'){
//                    saveButton.isVisible = true
//                    saveButton.text = getString(R.string.send_text)
//                    deleteButton.isVisible = true
//                    editButton.isVisible = true
//                    diaryHeadEditText.isEnabled = false
//                    diaryContentEditText.isEnabled = false
//                    diaryContentEditText.hint = getString(R.string.write_diary_content_100_hint)
//                }
//                else{
//                    //혼자 일기 수정이면
//                    if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
//                        saveButton.isVisible = false
//                        saveButton.text = getString(R.string.store_text)
//                        deleteButton.isVisible = true
//                        editButton.isVisible = true
//                        diaryHeadEditText.isEnabled = false
//                        diaryContentEditText.isEnabled = false
//                        diaryContentEditText.hint = getString(R.string.write_diary_content_hint)
//                    }
//                    //임시 저장 전송(edit api)이면
//                    else{
//                        findNavController().navigate(WriteDiaryFragmentDirections.actionWriteDiaryFragmentToCommentDiaryDetailFragment())
//                    }
//                }
//            }
//            else{
//                it.errorBody()?.let{ errorBody->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        if (it.status == 401) {
//                            Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
//                                .show()
//                            CodaApplication.getInstance().logOut()
//                        } else {
//                            CodaSnackBar.make(binding.root, "일기 수정에 실패하였습니다.").show()
//                        }
//                    }
//                }
//            }
        }

    }

    private fun initToolbar() = with(binding) {
        diaryTypeTextView.setOnClickListener {
            myDiaryViewModel.changeSelectDiaryTypeToolbarIsExpanded()
        }
    }

    private fun initViews() = with(binding){

    }

    @SuppressLint("ResourceAsColor")
    private fun changeViews(diaryType: DIARY_TYPE) = with(binding) {
        when(diaryType){
            DIARY_TYPE.COMMENT_DIARY ->{
                setCommentDiaryView()
            }
            DIARY_TYPE.ALONE_DIARY ->{
                setAloneDiaryView()
            }
        }
    }

    private fun setAloneDiaryView() = with(binding){
    }

    private fun setCommentDiaryView() = with(binding) {
    }

    private fun showBackDialog(){
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

//    private fun showSelectDiaryTypeDialog() {
//        val dialogView = Dialog(requireContext())
//        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialogView.setContentView(R.layout.dialog_mydiary_writediary)
//        dialogView.setCancelable(false)
//
//        dialogView.show()
//
//
//        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
//        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
//        val noticeTextView = dialogView.findViewById<TextView>(R.id.noticeTextView)
//        when(deleteOrSave){
//            "save"->{
//                noticeTextView.text = getString(R.string.diary_send_warning)
//            }
//            "delete"->{
//                noticeTextView.text = getString(R.string.diary_delete_warning)
//            }
//        }
//        submitButton.setOnClickListener {
//            when(deleteOrSave){
//                "save"->{
//                    if(binding.diaryContentEditText.text.toString().length<100){
//                        CodaSnackBar.make(binding.root, "내용을 100자 이상 입력해 주세요.").show()
//                        dialogView.dismiss()
//                        return@setOnClickListener
//                    }
//
//                    // 무조건 전송인 경우임, 동글뱅이 다이얼로그 하나 더 띄우고 저장 api호출 후 딜레이2초 후 동글뱅이 다이얼로그 종료
//
//                    launch(coroutineContext) {
//                        withContext(Dispatchers.IO) {
//                            //임시저장이 아닌 경우는 create
//                            if (myDiaryViewModel.selectedDiary.value!!.tempYN == 'N' || myDiaryViewModel.selectedDiary.value!!.tempYN == ' ') {
//                                myDiaryViewModel.setResponseSaveDiary(
//                                    SaveDiaryRequest(
//                                        title = binding.diaryHeadEditText.text.toString(),
//                                        content = binding.diaryContentEditText.text.toString(),
//                                        date = myDiaryViewModel.selectedDiary.value!!.date,
//                                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
//                                        tempYN = 'N'
//                                    )
//                                )
//                            } else {
//                                myDiaryViewModel.selectedDiary.value!!.tempYN = 'N'
//                                myDiaryViewModel.selectedDiary.value!!.id?.let { id ->
//                                    myDiaryViewModel.setResponseEditDiary(
//                                        diaryId = id,
//                                        editDiaryRequest = EditDiaryRequest(
//                                            binding.diaryHeadEditText.text.toString(),
//                                            binding.diaryContentEditText.text.toString(),
//                                            //무조건 N이긴 함
//                                            'N'
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                        dialogView.dismiss()
//                        showCircleDialog()
//                    }
//                }
//                "delete"->{
//                    //임시 저장 일기 삭제
//                    launch(coroutineContext) {
//                        binding.loadingBar.isVisible = true
//                        withContext(Dispatchers.IO) {
//                            myDiaryViewModel.selectedDiary.value!!.id?.let {
//                                myDiaryViewModel.setResponseDeleteDiary(it)
//                            }
//                        }
//                        dialogView.dismiss()
//                    }
//                }
//            }
//        }
//
//        cancelButton.setOnClickListener {
//            dialogView.dismiss()
//        }
//    }

    private suspend fun showCircleDialog() {

        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_mydiary_success_notice)
        dialogView.setCancelable(false)
        dialogView.show()
        delay(2000L)
        dialogView.dismiss()
    }
}