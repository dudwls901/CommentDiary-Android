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
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWritediaryBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.local.entity.LocalDiary
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.LocalDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WriteDiaryFragment : BaseFragment(), CoroutineScope, SelectDiaryTypeListener {

    override val TAG: String = WriteDiaryFragment::class.java.simpleName

    private lateinit var binding: FragmentMydiaryWritediaryBinding
    private lateinit var diaryTypeBottomSheet: SelectDiaryTypeBottomSheet
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val localDiaryViewModel: LocalDiaryViewModel by activityViewModels()

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object{
        fun newInstance() : WriteDiaryFragment {
            return WriteDiaryFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMydiaryWritediaryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.myDiaryviewModel = myDiaryViewModel
        //둘 다 안 해도 뷰모델 공유하고 해도 뷰모델 공유함 데이터바인딩은 안되는듯
//        binding.lifecycleOwner = this
        binding.lifecycleOwner = viewLifecycleOwner
        fragmentViewModel.setHasBottomNavi(false)

        initViews()
        initToolbar()
        changeViews()
        observeDatas()
        return binding.root
    }

    private fun observeDatas() = with(binding) {

        myDiaryViewModel.test.observe(viewLifecycleOwner){
            Log.d(TAG, "observeDatas: replace test초기화 test")
        }

        //todo responseSaveDiary.observe가 계속 감지돼서 일기 저장 후 다른 일기 작성하려할 때 commentDiaryDetail화면이 떠버림
        //저장은 혼자 쓴 일기, 코멘트 일기 둘 다 가능
        myDiaryViewModel.responseSaveDiary.observe(viewLifecycleOwner){
            Log.d(TAG, "observeDatas: replaceobserve ${it.body()}")
            Log.d(TAG, "observeDatas: replaceobserve ${it}")
            if(it.isSuccessful){
                Log.d(TAG, "observeDatas: 일기 저장 성공")
                //다이어리 셋팅
                myDiaryViewModel.setSelectedDiary(
                    Diary(
                        id = it.body()!!.result.id,
                        title = diaryHeadEditText.text.toString(),
                        content = diaryContentEditText.text.toString(),
                        date = myDiaryViewModel.selectedDiary.value!!.date,
                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                        commentList = null
                    )
                )

//                parentFragmentManager.popBackStack()
                //혼자 쓴 일기면
                if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
                    //수정,삭제 활성화, 전송 없애기, 텍스트 수정 불가
                    deleteButton.isVisible = true
                    editButton.isVisible = true
                    saveButton.isVisible = false
                    diaryHeadEditText.isEnabled = false
                    diaryContentEditText.isEnabled = false
                }
                //코멘트 일기면 화면 이동
                else{
//                    parentFragmentManager.popBackStack()
                    Log.d(TAG, "observeDatas responsesave: replace 여기가 불린다고?")
                    fragmentViewModel.setFragmentState("commentDiaryDetail")
                }
            }
            else{
                Log.d(TAG, "observeDatas: 일기 저장 실패")
            }
        }

        //일기 수정은 혼자 쓴 일기만 가능 (임시 저장 수정은 따로)
        myDiaryViewModel.responseEditDiary.observe(viewLifecycleOwner){
            if(it.isSuccessful){
                Log.d(TAG, "observeDatas: 일기 수정 성공")
                //다이어리 셋팅
                myDiaryViewModel.setSelectedDiary(
                    Diary(
                        id = myDiaryViewModel.selectedDiary.value!!.id,
                        title = diaryHeadEditText.text.toString(),
                        content = diaryContentEditText.text.toString(),
                        date = myDiaryViewModel.selectedDiary.value!!.date,
                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN,
                        commentList = myDiaryViewModel.selectedDiary.value!!.commentList
                    )
                )
                Log.d(TAG, "observeDatas responseedit: replace 여기가 불린다고?")
                fragmentViewModel.setFragmentState("commentDiaryDetail")
                deleteButton.isVisible = true
                editButton.isVisible = true
                saveButton.isVisible = false
                diaryHeadEditText.isEnabled = false
                diaryContentEditText.isEnabled = false

            }
            else{
                Log.d(TAG, "observeDatas: 일기 수정 실패")
            }
        }

    }


    @SuppressLint("ResourceAsColor")
    private fun changeViews() = with(binding){
        //혼자쓴 일기가 있는 경우
        if(myDiaryViewModel.selectedDiary.value!!.id!=null){
            saveButton.isVisible = false
            writeCommentDiaryTextLimitTextView.isVisible = false
            saveLocalButton.isVisible = false
            editButton.isVisible = true
            deleteButton.isVisible = true
            diaryUploadServerYetTextView.isVisible = false
            diaryContentEditText.isEnabled = false
            diaryHeadEditText.isEnabled = false
        }
        //id가 없는 경우
        else{
            myDiaryViewModel.setSaveOrEdit("save")
            //임시저장일기인 경우
            if(myDiaryViewModel.selectedDiary.value!!.content!=""){
                saveButton.text = getString(R.string.send_text)
                saveButton.isVisible = true
                saveButton.setBackgroundResource(R.drawable.background_pure_green_radius_30)
                editButton.isVisible = true
                deleteButton.isVisible = true
                saveLocalButton.isVisible = false
                diaryUploadServerYetTextView.isVisible = true
                diaryContentEditText.isEnabled = false
                diaryHeadEditText.isEnabled = false
                //이틀 지난 경우
                if(DateConverter.ymdToDate(myDiaryViewModel.selectedDiary.value!!.date) <=
                    DateConverter.getCodaToday().minusDays(2)){
                    diaryUploadServerYetTextView.text = getString(R.string.already_pass_diary_send_time)
                    editButton.isVisible = false
                    saveButton.setBackgroundResource(R.drawable.background_light_brown_radius_30)
                    saveButton.setTextColor(R.color.text_brown)
                    saveButton.isEnabled = false
                }
                else{
                    diaryUploadServerYetTextView.text = getString(R.string.upload_yet_comment_diary)
                    saveButton.setTextColor(R.color.background_ivory)
                    saveButton.isEnabled = true
                }
            }
            //일기가 없는 경우
            else{
                diaryContentEditText.isEnabled = true
                diaryHeadEditText.isEnabled = true
                saveButton.isVisible = true
                editButton.isVisible = false
                deleteButton.isVisible = false
                //혼자 일기 작성 화면
                if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='N'){
                    saveLocalButton.isVisible = false
                    saveButton.text = getString(R.string.store_text)
                    writeCommentDiaryTextLimitTextView.isVisible = false
                    diaryUploadServerYetTextView.isVisible = false
                }
                //코멘트 일기 작성 화면
                else{
                    saveLocalButton.isVisible = true
                    saveButton.text = getString(R.string.send_text)
                    writeCommentDiaryTextLimitTextView.isVisible = true
                    diaryUploadServerYetTextView.isVisible = true
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        Log.d("writeDiaryActivity", "initViews: myDiarViewModel.SaveOrEdit.value ${myDiaryViewModel.saveOrEdit.value}")
        //임시저장도 아니고 글이 아에 없는 경우만
        if (myDiaryViewModel.selectedDiary.value!!.id ==null && myDiaryViewModel.selectedDiary.value!!.content=="") {
            //바텀시트
            diaryTypeBottomSheet = SelectDiaryTypeBottomSheet(this@WriteDiaryFragment)
            diaryTypeBottomSheet.show(parentFragmentManager, "selectDiaryBottomSheet")
            diaryTypeBottomSheet.isCancelable = false
        }


        //글자수 카운트
        diaryContentEditText.addTextChangedListener {
            myDiaryViewModel.setCommentDiaryTextCount(diaryContentEditText.text.length)
        }

        saveButton.setOnClickListener {
            Log.d(TAG, "changeButtonEvent: ${myDiaryViewModel.saveOrEdit.value} ${myDiaryViewModel.selectedDiary.value!!.deliveryYN}")
            //제목,내용 벨리데이션 체크
            if(diaryHeadEditText.text.isEmpty()){
                Toast.makeText(requireContext(), "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(diaryContentEditText.text.isEmpty()){
                Toast.makeText(requireContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show()
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
                            //todo 저장되었습니다
                            launch(coroutineContext) {
                                myDiaryViewModel.setResponseSaveDiary(
                                    SaveDiaryRequest(
                                        title = diaryHeadEditText.text.toString(),
                                        content = diaryContentEditText.text.toString(),
                                        date = myDiaryViewModel.selectedDiary.value!!.date,
                                        deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN
                                    )
                                )
                            }
                            saveButton.isVisible = false
                            editButton.isVisible = true
                            deleteButton.isVisible = true
                        }
                    }
                }
                //일기 수정
                "edit"-> {
                    when(myDiaryViewModel.selectedDiary.value!!.deliveryYN){
                        //코멘트용 임시 저장 일기 수정(여기서 saveButton누르면 그냥 전송임)
                        'Y'->{
                            showDialog("save")
                        }
                        //혼자 일기 수정
                        'N'->{
                            //edit api실행
                            //성공한 경우 observer에서 view, myviewmodel.selecteddiary 변경
                            launch(coroutineContext) {
                                myDiaryViewModel.selectedDiary.value!!.id?.let { id ->
                                    myDiaryViewModel.setResponseEditDiary(
                                        diaryId = id,
                                        editDiaryRequest = EditDiaryRequest(
                                            diaryHeadEditText.text.toString(),
                                            diaryContentEditText.text.toString()
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

    private fun initToolbar() = with(binding){
        //뒤로가기
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //임시 저장
        saveLocalButton.setOnClickListener {
            //insert 충돌 설정값 때문에 수정은 없애도 될 거 같기도?
            if(myDiaryViewModel.selectedDiary.value!!.title==""){
                //저장
                localDiaryViewModel.saveDiary(LocalDiary(
                    date = myDiaryViewModel.selectedDiary.value!!.date,
                    title = binding.diaryHeadEditText.text.toString(),
                    content =binding.diaryContentEditText.text.toString(),
                    deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN
                ))
                Log.d(TAG, "localDialog 임시저장성공")
            }
            else{
                //수정
                localDiaryViewModel.editDiary(LocalDiary(
                    date = myDiaryViewModel.selectedDiary.value!!.date,
                    title = binding.diaryHeadEditText.text.toString(),
                    content =binding.diaryContentEditText.text.toString(),
                    deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN
                ))
                Log.d(TAG, "localDialog 임시저장수정성공")
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

            Log.d(TAG, "initToolbar: 여기 ${myDiaryViewModel.selectedDiary.value}")
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
                    // 무조건 전송인 경우임, 동글뱅이 다이얼로그 하나 더 띄우고 저장 api호출 후 딜레이2초 후 동글뱅이 다이얼로그 종료
                    launch(coroutineContext) {
                        myDiaryViewModel.setResponseSaveDiary(
                            SaveDiaryRequest(
                                title = binding.diaryHeadEditText.text.toString(),
                                content = binding.diaryContentEditText.text.toString(),
                                date = myDiaryViewModel.selectedDiary.value!!.date,
                                deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN
                            )
                        )
                        dialogView.dismiss()
                        showCircleDialog()
                    }
                }
                "delete"->{
                    //임시 저장 일기 삭제
                    Log.d(TAG, "showDialog: $deleteOrSave ${myDiaryViewModel.selectedDiary.value!!.deliveryYN}")
                    if(myDiaryViewModel.selectedDiary.value!!.deliveryYN=='Y'){
                        localDiaryViewModel.deleteDiary(
                            LocalDiary(
                                date = myDiaryViewModel.selectedDiary.value!!.date,
                                title = myDiaryViewModel.selectedDiary.value!!.title,
                                content =myDiaryViewModel.selectedDiary.value!!.content,
                                deliveryYN = myDiaryViewModel.selectedDiary.value!!.deliveryYN
                            )
                        )
                        Log.d(TAG, "showDialog: 임시 저장 일기 삭제 완료")
                        dialogView.dismiss()
                        //todo 전환 or popback
                        parentFragmentManager.popBackStack()
                    }
                    //혼자 일기 삭제 후 메인 화면
                    else{
                        launch(coroutineContext) {
                            myDiaryViewModel.selectedDiary.value!!.id?.let{
                                myDiaryViewModel.setResponseDeleteDiary(it)
                            }
                            dialogView.dismiss()
                            //todo 전환 or popback
                            parentFragmentManager.popBackStack()
                        }
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