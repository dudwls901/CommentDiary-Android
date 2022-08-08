package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WriteDiaryFragment : BaseFragment<FragmentMydiaryWritediaryBinding>(R.layout.fragment_mydiary_writediary), CoroutineScope, SelectDiaryTypeListener {

    override val TAG: String = WriteDiaryFragment::class.java.simpleName
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var diaryTypeBottomSheet: SelectDiaryTypeBottomSheet
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myDiaryviewModel = myDiaryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.WRITE_DIARY)

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

        myDiaryViewModel.isSavedDiary.observe(viewLifecycleOwner){
            //코멘트 일기 저장한 경우에 디테일 화면으로 이동
            if(it){
                if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='Y' &&
                    myDiaryViewModel.selectedDiary.value!!.tempYN=='N') {
                    val action =
                        WriteDiaryFragmentDirections.actionWriteDiaryFragmentToCommentDiaryDetailFragment()
                    findNavController().navigate(action)
                }
            }
        }

        //저장은 혼자 쓴 일기, 코멘트 일기 둘 다 가능
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner){ diary->
            //혼자 쓴 일기인 경우
            if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
                //수정,삭제 활성화, 전송 없애기, 텍스트 수정 불가
                deleteButton.isVisible = true
                editButton.isVisible = true
                saveButton.isVisible = false
                diaryHeadEditText.isEnabled = false
                diaryContentEditText.isEnabled = false
            }
            else if(myDiaryViewModel.selectedDiary.value!!.tempYN=='Y'){
                deleteButton.isVisible = true
                editButton.isVisible = true
                saveButton.isVisible = true
                diaryHeadEditText.isEnabled = false
                diaryContentEditText.isEnabled = false
            }
        }

        //todo 다 selectedDiaryobserve에서 처리?
        myDiaryViewModel.isEditedDiary.observe(viewLifecycleOwner){
            if(it){

            }
        }

        //혼자쓴 일기, 임시 저장 일기 수정
        myDiaryViewModel.responseEditDiary.observe(viewLifecycleOwner){
            binding.loadingBar.isVisible = false
            if(it.isSuccessful){
                CodaSnackBar.make(binding.root, "일기가 수정되었습니다.").show()
                //다이어리 셋팅
                myDiaryViewModel.setSelectedDiary(
                    Diary(
                        id = myDiaryViewModel.selectedDiary.value!!.id,
                        title = diaryHeadEditText.text.toString(),
                        content = diaryContentEditText.text.toString(),
                        date = myDiaryViewModel.selectedDiary.value!!.date,
                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                        tempYN = myDiaryViewModel.selectedDiary.value!!.tempYN,
                        commentList = myDiaryViewModel.selectedDiary.value!!.commentList
                    )
                )

                //임시 저장 수정이면
                if(myDiaryViewModel.selectedDiary.value!!.tempYN=='Y'){
                    saveButton.isVisible = true
                    saveButton.text = getString(R.string.send_text)
                    deleteButton.isVisible = true
                    editButton.isVisible = true
                    diaryHeadEditText.isEnabled = false
                    diaryContentEditText.isEnabled = false
                    diaryContentEditText.hint = getString(R.string.write_diary_content_100_hint)
                }
                else{
                    //혼자 일기 수정이면
                    if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
                        saveButton.isVisible = false
                        saveButton.text = getString(R.string.store_text)
                        deleteButton.isVisible = true
                        editButton.isVisible = true
                        diaryHeadEditText.isEnabled = false
                        diaryContentEditText.isEnabled = false
                        diaryContentEditText.hint = getString(R.string.write_diary_content_hint)
                    }
                    //임시 저장 전송(edit api)이면
                    else{
                        findNavController().navigate(WriteDiaryFragmentDirections.actionWriteDiaryFragmentToCommentDiaryDetailFragment())
                    }
                }
            }
            else{
                it.errorBody()?.let{ errorBody->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                                .show()
                            CodaApplication.getInstance().logOut()
                        } else {
                            CodaSnackBar.make(binding.root, "일기 수정에 실패하였습니다.").show()
                        }
                    }
                }
            }
        }

    }


    @SuppressLint("ResourceAsColor")
    private fun changeViews() = with(binding){
        Log.d(TAG, "changeViews: ${myDiaryViewModel.selectedDiary.value!!}")
        Log.d(TAG, "changeViews: ${myDiaryViewModel.selectedDiary.value!!.tempYN}")
        Log.d(TAG, "changeViews: ${myDiaryViewModel.saveOrEdit.value}")
        saveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.background_ivory))
        //일기가 있는 경우
        if(myDiaryViewModel.selectedDiary.value!!.id!=null) {
            //혼자쓴 일기가 있는 경우
            if (myDiaryViewModel.selectedDiary.value!!.deliveryYN == 'N') {
                //수정이면 저장하기 버튼 활성화
                if (myDiaryViewModel.saveOrEdit.value == "edit") {
                    saveButton.setBackgroundResource(R.drawable.background_green_radius_10)
                    saveButton.isVisible = true
                    editButton.isVisible = false
                    deleteButton.isVisible = false
                    diaryContentEditText.isEnabled = true
                    diaryHeadEditText.isEnabled = true
                }
                //그냥 보는 화면
                else {
                    saveButton.isVisible = false
                    editButton.isVisible = true
                    deleteButton.isVisible = true
                    diaryContentEditText.isEnabled = false
                    diaryHeadEditText.isEnabled = false
                }
                saveLocalButton.isVisible = false
                diaryUploadServerYetTextView.isVisible = false
                writeCommentDiaryTextLimitTextView.isVisible = false

            }
            //임시저장일기인 경우
            else {
                myDiaryViewModel.setSaveOrEdit("save")
                writeCommentDiaryTextLimitTextView.isVisible = true
                if (myDiaryViewModel.selectedDiary.value!!.content != "") {
                    saveButton.text = getString(R.string.send_text)
                    saveButton.isVisible = true
                    saveButton.setBackgroundResource(R.drawable.background_green_radius_10)
                    editButton.isVisible = true
                    deleteButton.isVisible = true
                    saveLocalButton.isVisible = false
                    diaryUploadServerYetTextView.isVisible = true
                    diaryContentEditText.isEnabled = false
                    diaryHeadEditText.isEnabled = false
                    //이틀 지난 경우
                    if (DateConverter.ymdToDate(myDiaryViewModel.selectedDiary.value!!.date) <=
                        DateConverter.getCodaToday().minusDays(2)
                    ) {
                        diaryUploadServerYetTextView.text =
                            getString(R.string.already_pass_diary_send_time)
                        editButton.isVisible = false
                        saveButton.setBackgroundResource(R.drawable.background_light_brown_radius_30)
                        saveButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.text_brown
                            )
                        )
                        saveButton.isEnabled = false
                    } else {
                        diaryUploadServerYetTextView.text =
                            getString(R.string.upload_temp_time_comment_diary)
                        saveButton.isEnabled = true
                    }
                }
            }
        }
        //일기가 없는 경우
        else{
            diaryContentEditText.isEnabled = true
            diaryHeadEditText.isEnabled = true
            saveButton.isVisible = true
            saveButton.setBackgroundResource(R.drawable.background_green_radius_10)
            saveButton.isEnabled = true
            editButton.isVisible = false
            deleteButton.isVisible = false
            //혼자 일기 작성 화면
            if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
                saveLocalButton.isVisible = false
                saveButton.text = getString(R.string.store_text)
                writeCommentDiaryTextLimitTextView.isVisible = false
                diaryUploadServerYetTextView.isVisible = false
                diaryContentEditText.hint = getString(R.string.write_diary_content_hint)
            }
            //코멘트 일기 작성 화면
            else{
                saveLocalButton.isVisible = true
                saveButton.text = getString(R.string.send_text)
                writeCommentDiaryTextLimitTextView.isVisible = true
                diaryUploadServerYetTextView.isVisible = true
                diaryContentEditText.hint = getString(R.string.write_diary_content_100_hint)
            }
        }
        saveButton.setOnClickListener {
            Log.d(TAG, "changeViews: savebuttonclick ${myDiaryViewModel.saveOrEdit.value}")
            //제목,내용 벨리데이션 체크
            if(diaryHeadEditText.text.isEmpty()){
                CodaSnackBar.make(binding.root, "제목을 입력해 주세요.").show()
                return@setOnClickListener
            }
            else if(diaryContentEditText.text.isEmpty()){
                CodaSnackBar.make(binding.root, "내용을 입력해 주세요.").show()
                return@setOnClickListener
            }
            when(myDiaryViewModel.saveOrEdit.value){
                "save"->{
                    when(myDiaryViewModel.selectedDiary.value!!.deliveryYN){
                        //코멘트 일기 전송 버튼 누른 경우
                        'Y'->{
                            //다이얼로그 띄우고 확인 누르면 코멘트 일기 디테일 화면으로 넘기기
                            showDialog("save")
                        }
                        //혼자 일기는 그냥 전송
                        'N'-> {
                            //정상 저장(api 호출)
                            binding.loadingBar.isVisible = true
                            launch(coroutineContext) {
                                withContext(Dispatchers.IO) {
                                    myDiaryViewModel.setResponseSaveDiary(
                                        SaveDiaryRequest(
                                            title = diaryHeadEditText.text.toString(),
                                            content = diaryContentEditText.text.toString(),
                                            date = myDiaryViewModel.selectedDiary.value!!.date,
                                            deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                                            tempYN = 'N'
                                        )
                                    )
                                }
                                saveButton.isVisible = false
                                editButton.isVisible = true
                                deleteButton.isVisible = true
                            }
                        }
                    }
                }
                //일기 수정
                "edit"-> {
                    when(myDiaryViewModel.selectedDiary.value!!.deliveryYN){
                        //코멘트용 임시 저장 일기 수정
                        'Y'->{
                            showDialog("save")
                        }
                        //혼자 일기 수정
                        'N'->{
                            //edit api실행
                            //성공한 경우 observer에서 view, myviewmodel.selecteddiary 변경
                            launch(coroutineContext) {
                                binding.loadingBar.isVisible = true
                                withContext(Dispatchers.IO) {
                                    myDiaryViewModel.selectedDiary.value!!.id?.let { id ->
                                        myDiaryViewModel.setResponseEditDiary(
                                            diaryId = id,
                                            editDiaryRequest = EditDiaryRequest(
                                                diaryHeadEditText.text.toString(),
                                                diaryContentEditText.text.toString(),
                                                'N'
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        //현재 글자수 바로 보여주기
        myDiaryViewModel.setCommentDiaryTextCount(diaryContentEditText.text.length)

        //임시저장도 아니고 글이 아에 없는 경우만
        if (myDiaryViewModel.selectedDiary.value!!.id ==null && myDiaryViewModel.selectedDiary.value!!.content=="") {
            //오늘 날짜면 바텀시트로 선택
            if(DateConverter.ymdToDate(myDiaryViewModel.selectedDiary.value!!.date) == DateConverter.getCodaToday()){
                //바텀시트
                diaryTypeBottomSheet = SelectDiaryTypeBottomSheet(this@WriteDiaryFragment)
                diaryTypeBottomSheet.show(parentFragmentManager, "selectDiaryBottomSheet")
                diaryTypeBottomSheet.isCancelable = false
            }
            //과거 날짜면 혼자 쓰는 일기만 가능
            else{
                myDiaryViewModel.setDeliveryYN('N')
            }

        }

        //글자수 카운트
        diaryContentEditText.addTextChangedListener {
            myDiaryViewModel.setCommentDiaryTextCount(diaryContentEditText.text.length)
        }

        changeViews()
    }

    private fun initToolbar() = with(binding){
        //뒤로가기
        backButton.setOnClickListener {
            myDiaryViewModel.setSaveOrEdit("save")
            //편집 가능한 상황, 이전 내용에서 편집을 한 상황
            if(diaryContentEditText.isEnabled &&
                (diaryContentEditText.text.toString() !=myDiaryViewModel.selectedDiary.value!!.content||
                        diaryHeadEditText.text.toString() !=myDiaryViewModel.selectedDiary.value!!.title)
            ){
                showBackDialog()
            }
            else{
                findNavController().popBackStack()
            }

        }

        //임시 저장
        saveLocalButton.setOnClickListener {
            //제목,내용 벨리데이션 체크
            if(diaryHeadEditText.text.isEmpty()){
                CodaSnackBar.make(binding.root, "제목을 입력해 주세요.").show()
                return@setOnClickListener
            }
            else if(diaryContentEditText.text.isEmpty()){
                CodaSnackBar.make(binding.root, "내용을 입력해 주세요.").show()
                return@setOnClickListener
            }
            binding.loadingBar.isVisible = true
            //insert 충돌 설정값 때문에 수정은 없애도 될 거 같기도?
            if(myDiaryViewModel.selectedDiary.value!!.title==""){
                //저장
                myDiaryViewModel.selectedDiary.value!!.tempYN = 'Y'
                launch(coroutineContext) {
                    withContext(Dispatchers.IO) {
                        myDiaryViewModel.setResponseSaveDiary(
                            SaveDiaryRequest(
                                title = diaryHeadEditText.text.toString(),
                                content = diaryContentEditText.text.toString(),
                                date = myDiaryViewModel.selectedDiary.value!!.date,
                                deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                                tempYN = 'Y'
                            )
                        )
                    }
                }
            }
            else{
                //수정
                launch(coroutineContext) {
                    withContext(Dispatchers.IO) {
                        myDiaryViewModel.selectedDiary.value!!.id?.let { id ->
                            myDiaryViewModel.setResponseEditDiary(
                                diaryId = id,
                                editDiaryRequest = EditDiaryRequest(
                                    diaryHeadEditText.text.toString(),
                                    diaryContentEditText.text.toString(),
                                    myDiaryViewModel.selectedDiary.value!!.tempYN
                                )
                            )
                        }
                    }
                }
            }
            deleteButton.isVisible = true
            editButton.isVisible = true
            saveLocalButton.isVisible = false
            diaryContentEditText.isEnabled = false
            diaryHeadEditText.isEnabled = false
        }

        deleteButton.setOnClickListener {
            //임시저장 삭제 or 삭제api
            showDialog("delete")
        }

        editButton.setOnClickListener {
            //버튼동작을 수정 api로
            myDiaryViewModel.setSaveOrEdit("edit")

            binding.deleteButton.isVisible = false
            binding.editButton.isVisible = false
            binding.saveButton.isVisible = true
            binding.diaryContentEditText.isEnabled = true
            binding.diaryHeadEditText.isEnabled = true
            if(myDiaryViewModel.selectedDiary.value!!.deliveryYN =='Y'){
                binding.saveButton.text = getString(R.string.send_text)
                binding.saveLocalButton.isVisible = true
            }
            else{
                diaryUploadServerYetTextView.isVisible = false
                binding.saveButton.text = getString(R.string.store_text)
            }
        }
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

    private fun showDialog(deleteOrSave: String) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_mydiary_writediary)
        dialogView.setCancelable(false)

        dialogView.show()


        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val noticeTextView = dialogView.findViewById<TextView>(R.id.noticeTextView)
        when(deleteOrSave){
            "save"->{
                noticeTextView.text = getString(R.string.diary_send_warning)
            }
            "delete"->{
                noticeTextView.text = getString(R.string.diary_delete_warning)
            }
        }
        submitButton.setOnClickListener {
            when(deleteOrSave){
                "save"->{
                    if(binding.diaryContentEditText.text.toString().length<100){
                        CodaSnackBar.make(binding.root, "내용을 100자 이상 입력해 주세요.").show()
                        dialogView.dismiss()
                        return@setOnClickListener
                    }

                    // 무조건 전송인 경우임, 동글뱅이 다이얼로그 하나 더 띄우고 저장 api호출 후 딜레이2초 후 동글뱅이 다이얼로그 종료

                    launch(coroutineContext) {
                        withContext(Dispatchers.IO) {
                            //임시저장이 아닌 경우는 create
                            if (myDiaryViewModel.selectedDiary.value!!.tempYN == 'N' || myDiaryViewModel.selectedDiary.value!!.tempYN == ' ') {
                                myDiaryViewModel.setResponseSaveDiary(
                                    SaveDiaryRequest(
                                        title = binding.diaryHeadEditText.text.toString(),
                                        content = binding.diaryContentEditText.text.toString(),
                                        date = myDiaryViewModel.selectedDiary.value!!.date,
                                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                                        tempYN = 'N'
                                    )
                                )
                            } else {
                                myDiaryViewModel.selectedDiary.value!!.tempYN = 'N'
                                myDiaryViewModel.selectedDiary.value!!.id?.let { id ->
                                    myDiaryViewModel.setResponseEditDiary(
                                        diaryId = id,
                                        editDiaryRequest = EditDiaryRequest(
                                            binding.diaryHeadEditText.text.toString(),
                                            binding.diaryContentEditText.text.toString(),
                                            //무조건 N이긴 함
                                            'N'
                                        )
                                    )
                                }
                            }
                        }
                        dialogView.dismiss()
                        showCircleDialog()
                    }
                }
                "delete"->{
                    //임시 저장 일기 삭제
                    launch(coroutineContext) {
                        binding.loadingBar.isVisible = true
                        withContext(Dispatchers.IO) {
                            myDiaryViewModel.selectedDiary.value!!.id?.let {
                                myDiaryViewModel.setResponseDeleteDiary(it)
                            }
                        }
                        dialogView.dismiss()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            dialogView.dismiss()
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
    }

    override fun onSelectDiaryTypeListener(deliveryYN: Char) {
        changeViews()
    }
}