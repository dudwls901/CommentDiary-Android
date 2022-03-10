package com.movingmaker.commentdiary.view.main.mydiary

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.service.autofill.SaveRequest
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWritediaryBinding
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.view.main.mypage.TempMyPageFragment
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WriteDiaryFragment : BaseFragment(), CoroutineScope {

    override val TAG: String = WriteDiaryFragment::class.java.simpleName

    private lateinit var binding: FragmentMydiaryWritediaryBinding
    private lateinit var diaryTypeBottomSheet: SelectDiaryTypeBottomSheet
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
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
        binding.lifecycleOwner = this
        fragmentViewModel.setHasBottomNavi(false)
        Log.d(TAG, "onCreateView saveOrEdit: ${myDiaryViewModel.saveOrEdit.value}")
        initViews()
        changeViews(myDiaryViewModel.deliveryYN.value!!)
        observeDatas()
        return binding.root
    }


    private fun observeDatas() = with(binding) {

        myDiaryViewModel.responseSaveDiary.observe(viewLifecycleOwner){
            if(it.isSuccessful){
                Log.d(TAG, "observeDatas: 일기 저장 성공")
//                parentFragmentManager.popBackStack()
//                deleteButton.isVisible = true
//                editButton.isVisible = true
//                saveButton.isVisible = false
            }
            else{

            }
        }

        myDiaryViewModel.responseEditDiary.observe(viewLifecycleOwner){
            if(it.isSuccessful){
                Log.d(TAG, "observeDatas: 일기 수정 성공")

            }
            else{

            }
        }

        myDiaryViewModel.deliveryYN.observe(viewLifecycleOwner) { deliveryYN ->
            Log.d(TAG, "observeDatas: changebutton ${myDiaryViewModel.deliveryYN.value} ${myDiaryViewModel.selectedDiary.value!!.id}")
            changeViews(deliveryYN)
        }
    }

    private fun changeViews(deliveryYN: Char){
        Log.d(TAG, "observeDatas: changeview : ${deliveryYN}")
        when (deliveryYN) {
            //혼자 일기
            'N' -> {
                //save api
                Log.d(TAG, "observeDatas: saveOrEdit ${deliveryYN}")
                Log.d(TAG, "observeDatas: ${myDiaryViewModel.saveOrEdit.value}")
                when(myDiaryViewModel.saveOrEdit.value){

                    //혼자일기 저장
                    "save"->{
                        Log.d(TAG, "observeDatas: save")
                        changeButtonEvent("save")
                        binding.deleteButton.isVisible = false
                        binding.editButton.isVisible = false
                        binding.saveLocalButton.isVisible = false
                        binding.writeCommentDiaryNoticeTextView.isVisible = false
                        binding.writeCommentDiaryTextLimitTextView.isVisible = false
                        binding.saveButton.isVisible = true
                        binding.saveButton.text = getString(R.string.store_text)
                    }
                    //혼자일기 수정
                    "edit"->{
                        Log.d(TAG, "observeDatas: edit")
                        binding.deleteButton.isVisible = true
                        binding.editButton.isVisible = true
                        binding.saveLocalButton.isVisible = false
                        binding.writeCommentDiaryNoticeTextView.isVisible = false
                        binding.writeCommentDiaryTextLimitTextView.isVisible = false
                        binding.saveButton.isVisible = false
                    }
                }
            }
            'Y' -> {
                //edit api
                when(myDiaryViewModel.saveOrEdit.value){
                    //코멘트일기 저장
                    "save"->{
                        changeButtonEvent("save")
                        binding.deleteButton.isVisible = false
                        binding.editButton.isVisible = false
                        binding.saveLocalButton.isVisible = true
                        binding.writeCommentDiaryNoticeTextView.isVisible = true
                        binding.writeCommentDiaryTextLimitTextView.isVisible = true
                        binding.saveButton.isVisible = true
                        binding.saveButton.text = getString(R.string.send_text)
                    }
                    //코멘트일기 수정
                    "edit"->{
                        changeButtonEvent("edit")
                        binding.deleteButton.isVisible = true
                        binding.editButton.isVisible = true
                        binding.saveLocalButton.isVisible = false
                        binding.writeCommentDiaryNoticeTextView.isVisible = false
                        binding.writeCommentDiaryTextLimitTextView.isVisible = false
                        binding.saveButton.isVisible = true
                        binding.saveButton.text = getString(R.string.send_text)
                        binding.diaryUploadServerYetTextView.isVisible = true
                    }
                }
            }
        }
    }

    private fun changeButtonEvent(saveOrEdit: String) = with(binding){
        Log.d(TAG, "changeButtonEvent: ${saveOrEdit} ${myDiaryViewModel.deliveryYN.value}")
        saveButton.setOnClickListener {
            Log.d(TAG, "changeButtonEvent: ${saveOrEdit} ${myDiaryViewModel.deliveryYN.value}")
            when(saveOrEdit){
                "save"->{
                    when(myDiaryViewModel.deliveryYN.value){
                        //코멘트 일기 전송 버튼 누른 경우
                        'Y'->{
                            //todo 다이얼로그 띄우고 확인 누르면 코멘트 일기 디테일 화면으로 넘기기
                            showDialog("save")
                            Log.d(TAG, "changeButtonEvent: ${saveOrEdit} ${myDiaryViewModel.deliveryYN.value}")
                        }
                        //혼자 일기는 그냥 전송
                        'N'->{
                            //todo 저장되었습니다
                            launch(coroutineContext) {
                                myDiaryViewModel.setResponseSaveDiary(
                                    SaveDiaryRequest(
                                        title = diaryHeadEditText.text.toString(),
                                        content = diaryContentEditText.text.toString(),
                                        date = myDiaryViewModel.selectedDiary.value!!.date,
                                        deliveryYN = myDiaryViewModel.deliveryYN.value!!
                                    )
                                )
                            }
                        }
                    }
                }
                "edit"-> {
                    when(myDiaryViewModel.deliveryYN.value){
                        //코멘트용 일기 수정
                        'Y'->{
                            //todo room에 저장
                        }
                        //혼자 일기 수정
                        'N'->{
                            //edit api실행
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
                                myDiaryViewModel.setSaveOrEdit("save")
                                myDiaryViewModel.setDeliveryYN(myDiaryViewModel.deliveryYN.value!!)
//                        binding.deleteButton.isVisible = true
//                        binding.editButton.isVisible = true
//                        binding.saveLocalButton.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        Log.d("writeDiaryActivity", "initViews: myDiarViewModel.SaveOrEdit.value ${myDiaryViewModel.saveOrEdit.value}")
        if (myDiaryViewModel.saveOrEdit.value == "save") {
            //바텀시트
            diaryTypeBottomSheet = SelectDiaryTypeBottomSheet.newInstance()
            diaryTypeBottomSheet.show(parentFragmentManager, "selectDiaryBottomSheet")
            diaryTypeBottomSheet.isCancelable = false
        }

        initToolbar()

        diaryContentEditText.addTextChangedListener {
            myDiaryViewModel.setCommentDiaryTextCount(diaryContentEditText.text.length)
        }

    }

    private fun initToolbar() = with(binding){
        //뒤로가기
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //임시 저장
        saveLocalButton.setOnClickListener {

        }

        deleteButton.setOnClickListener {
            //삭제 api
            showDialog("delete")
        }

        editButton.setOnClickListener {
            myDiaryViewModel.setSaveOrEdit("edit")
            binding.deleteButton.isVisible = false
            binding.editButton.isVisible = false
            binding.saveLocalButton.isVisible = true
        }
    }


    //todo 테두리 둥글게
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
                    //todo 무조건 전송인 경우임, 동글뱅이 다이얼로그 하나 더 띄우고 저장 api호출 후 딜레이2초 후 동글뱅이 다이얼로그 종료
                    launch(coroutineContext) {
                        myDiaryViewModel.setResponseSaveDiary(
                            SaveDiaryRequest(
                                title = binding.diaryHeadEditText.text.toString(),
                                content = binding.diaryContentEditText.text.toString(),
                                date = myDiaryViewModel.selectedDiary.value!!.date,
                                deliveryYN = myDiaryViewModel.deliveryYN.value!!
                            )
                        )
                        dialogView.dismiss()
                        showCircleDialog()
                    }
                }
                "delete"->{
                    //todo 임시 삭제, 삭제 후 동작은 그냥 메인화면?
                    if(myDiaryViewModel.deliveryYN.value=='Y'){

                    }
                    //todo (혼자 일기 삭제) 일기 삭제 api 호출
                    else{

                    }
                }
            }
        }

    //todo 취소 누르고 다시 전송하기 누르면 동작을 안해버림
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
}