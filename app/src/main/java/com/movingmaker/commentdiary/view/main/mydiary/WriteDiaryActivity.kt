package com.movingmaker.commentdiary.view.main.mydiary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.movingmaker.commentdiary.databinding.ActivityMydiaryWritediaryBinding
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel

class WriteDiaryActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMydiaryWritediaryBinding
    private lateinit var diaryTypeBottomSheet: SelectDiaryTypeBottomSheet
    private val myDiaryViewModel: MyDiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMydiaryWritediaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.myDiaryviewModel = myDiaryViewModel
        binding.lifecycleOwner = this
        initViews()
        observeDatas()
    }

    private fun observeDatas(){
        myDiaryViewModel.saveOrEdit.observe(this) {
            when(it){
                //혼자 일기
                "save"->{
                    //save api
                }
                "edit"->{
                    //edit api
                }
            }
        }
    }

    private fun initViews() = with(binding){
        if(myDiaryViewModel.saveOrEdit.value =="save"){
            //바텀시트
            diaryTypeBottomSheet = SelectDiaryTypeBottomSheet.newInstance()
            diaryTypeBottomSheet.show(supportFragmentManager,"selectDiaryBottomSheet")
            diaryTypeBottomSheet.isCancelable = false
        }

        //툴바
        backButton.setOnClickListener {
            finish()
        }

        deleteButton.setOnClickListener {
            //삭제 api

        }

        editButton.setOnClickListener {
            //edit api
        }
        diaryContentEditText.addTextChangedListener{
            myDiaryViewModel.setCommentDiaryTextCount(diaryContentEditText.text.length)
        }


    }

}